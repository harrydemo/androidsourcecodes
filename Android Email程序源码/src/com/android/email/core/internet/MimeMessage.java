/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.email.core.internet;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.james.mime4j.MimeException;
import org.apache.james.mime4j.descriptor.BodyDescriptor;
import org.apache.james.mime4j.field.DateTimeField;
import org.apache.james.mime4j.field.DefaultFieldParser;
import org.apache.james.mime4j.io.EOLConvertingInputStream;
import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.parser.Field;
import org.apache.james.mime4j.parser.MimeStreamParser;
import org.apache.james.mime4j.util.ContentUtil;

import com.android.email.core.mail.Address;
import com.android.email.core.mail.Body;
import com.android.email.core.mail.BodyPart;
import com.android.email.core.mail.Message;
import com.android.email.core.mail.MessagingException;
import com.android.email.core.mail.Multipart;
import com.android.email.core.mail.Part;


/**
 * An implementation of Message that stores all of it's metadata in RFC 822 and
 * RFC 2045 style headers.
 */
public class MimeMessage extends Message {
    protected MimeHeader mHeader = new MimeHeader();
    
    // NOTE:  The fields here are transcribed out of headers, and values stored here will supercede
    // the values found in the headers.  Use caution to prevent any out-of-phase errors.  In
    // particular, any adds/changes/deletes here must be echoed by changes in the parse() function.
    protected Address[] mFrom;
    protected Address[] mTo;
    protected Address[] mCc;
    protected Address[] mBcc;
    protected Address[] mReplyTo;
    protected Date mSentDate;
    
    // In MIME, en_US-like date format should be used. In other words "MMM" should be encoded to
    // "Jan", not the other localized format like "Ene" (meaning January in locale es).
    // This conversion is used when generating outgoing MIME messages. Incoming MIME date
    // headers are parsed by org.apache.james.mime4j.field.DateTimeField which does not have any
    // localization code.
    protected SimpleDateFormat mDateFormat = 
        new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.US);
    protected Body mBody;
    protected int mSize;

    // regex that matches content id surrounded by "<>" optionally.
    private static final Pattern REMOVE_OPTIONAL_BRACKETS = Pattern.compile("^<?([^>]+)>?$");

    public MimeMessage() {
        /*
         * Every new messages gets a Message-ID
         */
        try {
            // TODO: This is wasteful, since we overwrite it on incoming or locally-read messages.
            // Should only generate it on as-needed basis.
            setMessageId(generateMessageId());
        }
        catch (MessagingException me) {
            throw new RuntimeException("Unable to create MimeMessage", me);
        }
    }

    private String generateMessageId() {
        StringBuffer sb = new StringBuffer();
        sb.append("<");
        for (int i = 0; i < 24; i++) {
            sb.append(Integer.toString((int)(Math.random() * 35), 36));
        }
        sb.append(".");
        sb.append(Long.toString(System.currentTimeMillis()));
        sb.append("@email.android.com>");
        return sb.toString();
    }

    /**
     * Parse the given InputStream using Apache Mime4J to build a MimeMessage.
     *
     * @param in
     * @throws IOException
     * @throws MessagingException
     * @throws MimeException 
     */
    public MimeMessage(InputStream in) throws IOException, MessagingException, MimeException {
        parse(in);
    }

    protected void parse(InputStream in) throws IOException, MessagingException, MimeException {
        // Before parsing the input stream, clear all local fields that may be superceded by
        // the new incoming message.
        mHeader.clear();
        mFrom = null;
        mTo = null;
        mCc = null;
        mBcc = null;
        mReplyTo = null;
        mSentDate = null;
        mBody = null;

        MimeStreamParser parser = new MimeStreamParser();
        parser.setContentHandler(new MimeMessageBuilder());
        parser.parse(new EOLConvertingInputStream(in));
    }

    public Date getReceivedDate() throws MessagingException {
        return null;
    }

    public Date getSentDate() throws MessagingException {
        if (mSentDate == null) {
            try {
                DateTimeField field = (DateTimeField)parse("Date: "
                        + MimeUtility.unfoldAndDecode(getFirstHeader("Date")));
                mSentDate = field.getDate();
            } catch (Exception e) {

            }
        }
        return mSentDate;
    }

    public void setSentDate(Date sentDate) throws MessagingException {
        setHeader("Date", mDateFormat.format(sentDate));
        this.mSentDate = sentDate;
    }

    public String getContentType() throws MessagingException {
        String contentType = getFirstHeader(MimeHeader.HEADER_CONTENT_TYPE);
        if (contentType == null) {
            return "text/plain";
        } else {
            return contentType;
        }
    }

    public String getDisposition() throws MessagingException {
        String contentDisposition = getFirstHeader(MimeHeader.HEADER_CONTENT_DISPOSITION);
        if (contentDisposition == null) {
            return null;
        } else {
            return contentDisposition;
        }
    }

    public String getContentId() throws MessagingException {
        String contentId = getFirstHeader(MimeHeader.HEADER_CONTENT_ID);
        if (contentId == null) {
            return null;
        } else {
            // remove optionally surrounding brackets.
            return REMOVE_OPTIONAL_BRACKETS.matcher(contentId).replaceAll("$1");
        }
    }

    public String getMimeType() throws MessagingException {
        return MimeUtility.getHeaderParameter(getContentType(), null);
    }

    public int getSize() throws MessagingException {
        return mSize;
    }

    /**
     * Returns a list of the given recipient type from this message. If no addresses are
     * found the method returns an empty array.
     */
    public Address[] getRecipients(RecipientType type) throws MessagingException {
        if (type == RecipientType.TO) {
            if (mTo == null) {
                mTo = Address.parse(MimeUtility.unfold(getFirstHeader("To")));
            }
            return mTo;
        } else if (type == RecipientType.CC) {
            if (mCc == null) {
                mCc = Address.parse(MimeUtility.unfold(getFirstHeader("CC")));
            }
            return mCc;
        } else if (type == RecipientType.BCC) {
            if (mBcc == null) {
                mBcc = Address.parse(MimeUtility.unfold(getFirstHeader("BCC")));
            }
            return mBcc;
        } else {
            throw new MessagingException("Unrecognized recipient type.");
        }
    }

    public void setRecipients(RecipientType type, Address[] addresses) throws MessagingException {
        if (type == RecipientType.TO) {
            if (addresses == null || addresses.length == 0) {
                removeHeader("To");
                this.mTo = null;
            } else {
                setHeader("To", Address.toString(addresses));
                this.mTo = addresses;
            }
        } else if (type == RecipientType.CC) {
            if (addresses == null || addresses.length == 0) {
                removeHeader("CC");
                this.mCc = null;
            } else {
                setHeader("CC", Address.toString(addresses));
                this.mCc = addresses;
            }
        } else if (type == RecipientType.BCC) {
            if (addresses == null || addresses.length == 0) {
                removeHeader("BCC");
                this.mBcc = null;
            } else {
                setHeader("BCC", Address.toString(addresses));
                this.mBcc = addresses;
            }
        } else {
            throw new MessagingException("Unrecognized recipient type.");
        }
    }

    /**
     * Returns the unfolded, decoded value of the Subject header.
     */
    public String getSubject() throws MessagingException {
        return MimeUtility.unfoldAndDecode(getFirstHeader("Subject"));
    }

    public void setSubject(String subject) throws MessagingException {
        final int HEADER_NAME_LENGTH = 9;     // "Subject: "
        setHeader("Subject", MimeUtility.foldAndEncode2(subject, HEADER_NAME_LENGTH));
    }

    public Address[] getFrom() throws MessagingException {
        if (mFrom == null) {
            String list = MimeUtility.unfold(getFirstHeader("From"));
            if (list == null || list.length() == 0) {
                list = MimeUtility.unfold(getFirstHeader("Sender"));
            }
            mFrom = Address.parse(list);
        }
        return mFrom;
    }

    public void setFrom(Address from) throws MessagingException {
        if (from != null) {
            setHeader("From", from.toString());
            this.mFrom = new Address[] {
                    from
                };
        } else {
            this.mFrom = null;
        }
    }

    public Address[] getReplyTo() throws MessagingException {
        if (mReplyTo == null) {
            mReplyTo = Address.parse(MimeUtility.unfold(getFirstHeader("Reply-to")));
        }
        return mReplyTo;
    }

    public void setReplyTo(Address[] replyTo) throws MessagingException {
        if (replyTo == null || replyTo.length == 0) {
            removeHeader("Reply-to");
            mReplyTo = null;
        } else {
            setHeader("Reply-to", Address.toString(replyTo));
            mReplyTo = replyTo;
        }
    }
    
    /**
     * Set the mime "Message-ID" header
     * @param messageId the new Message-ID value
     * @throws MessagingException
     */
    public void setMessageId(String messageId) throws MessagingException {
        setHeader("Message-ID", messageId);
    }
    
    /**
     * Get the mime "Message-ID" header.  Note, this field is preset (randomly) in every new 
     * message, so it should never return null.
     * @return the Message-ID header string
     * @throws MessagingException
     */
    public String getMessageId() throws MessagingException {
        String[] headers = getHeader("Message-ID");
        if (headers != null) {
            // There should really only be one Message-ID here
            return headers[0];
        }
        throw new MessagingException("A message was found without a Message-ID header");
    }

    public void saveChanges() throws MessagingException {
        throw new MessagingException("saveChanges not yet implemented");
    }

    public Body getBody() throws MessagingException {
        return mBody;
    }

    public void setBody(Body body) throws MessagingException {
        this.mBody = body;
        if (body instanceof Multipart) {
            Multipart multipart = ((Multipart)body);
            multipart.setParent(this);
            setHeader(MimeHeader.HEADER_CONTENT_TYPE, multipart.getContentType());
            setHeader("MIME-Version", "1.0");
        }
        else if (body instanceof TextBody) {
            setHeader(MimeHeader.HEADER_CONTENT_TYPE, String.format("%s;\n charset=utf-8",
                    getMimeType()));
            setHeader(MimeHeader.HEADER_CONTENT_TRANSFER_ENCODING, "base64");
        }
    }

    protected String getFirstHeader(String name) throws MessagingException {
        return mHeader.getFirstHeader(name);
    }

    public void addHeader(String name, String value) throws MessagingException {
        mHeader.addHeader(name, value);
    }

    public void setHeader(String name, String value) throws MessagingException {
        mHeader.setHeader(name, value);
    }

    public String[] getHeader(String name) throws MessagingException {
        return mHeader.getHeader(name);
    }

    public void removeHeader(String name) throws MessagingException {
        mHeader.removeHeader(name);
    }

    public void writeTo(OutputStream out) throws IOException, MessagingException {
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out), 1024);
        mHeader.writeTo(out);
        writer.write("\r\n");
        writer.flush();
        if (mBody != null) {
            mBody.writeTo(out);
        }
    }

    public InputStream getInputStream() throws MessagingException {
        return null;
    }

    class MimeMessageBuilder implements ContentHandler {
        private Stack stack = new Stack();

        public MimeMessageBuilder() {
        }

        private void expect(Class c) {
            if (!c.isInstance(stack.peek())) {
                throw new IllegalStateException("Internal stack error: " + "Expected '"
                        + c.getName() + "' found '" + stack.peek().getClass().getName() + "'");
            }
        }

        public void startMessage() {
            if (stack.isEmpty()) {
                stack.push(MimeMessage.this);
            } else {
                expect(Part.class);
                try {
                    MimeMessage m = new MimeMessage();
                    ((Part)stack.peek()).setBody(m);
                    stack.push(m);
                } catch (MessagingException me) {
                    throw new Error(me);
                }
            }
        }

        public void endMessage() {
            expect(MimeMessage.class);
            stack.pop();
        }

        public void startHeader() {
            expect(Part.class);
        }

        public void endHeader() {
            expect(Part.class);
        }

        public void startMultipart(BodyDescriptor bd) {
            expect(Part.class);

            Part e = (Part)stack.peek();
            try {
                MimeMultipart multiPart = new MimeMultipart(e.getContentType());
                e.setBody(multiPart);
                stack.push(multiPart);
            } catch (MessagingException me) {
                throw new Error(me);
            }
        }

        public void body(BodyDescriptor bd, InputStream in) throws IOException {
            expect(Part.class);
            Body body = MimeUtility.decodeBody(in, bd.getTransferEncoding());
            try {
                ((Part)stack.peek()).setBody(body);
            } catch (MessagingException me) {
                throw new Error(me);
            }
        }

        public void endMultipart() {
            stack.pop();
        }

        public void startBodyPart() {
            expect(MimeMultipart.class);

            try {
                MimeBodyPart bodyPart = new MimeBodyPart();
                ((MimeMultipart)stack.peek()).addBodyPart(bodyPart);
                stack.push(bodyPart);
            } catch (MessagingException me) {
                throw new Error(me);
            }
        }

        public void endBodyPart() {
            expect(BodyPart.class);
            stack.pop();
        }

        public void epilogue(InputStream is) throws IOException {
            expect(MimeMultipart.class);
            StringBuffer sb = new StringBuffer();
            int b;
            while ((b = is.read()) != -1) {
                sb.append((char)b);
            }
            // ((Multipart) stack.peek()).setEpilogue(sb.toString());
        }

        public void preamble(InputStream is) throws IOException {
            expect(MimeMultipart.class);
            StringBuffer sb = new StringBuffer();
            int b;
            while ((b = is.read()) != -1) {
                sb.append((char)b);
            }
            try {
                ((MimeMultipart)stack.peek()).setPreamble(sb.toString());
            } catch (MessagingException me) {
                throw new Error(me);
            }
        }

        public void raw(InputStream is) throws IOException {
            throw new UnsupportedOperationException("Not supported");
        }

		public void field(String fieldData) throws MimeException {
			expect(Part.class);
            try {
                String[] tokens = fieldData.split(":", 2);
                ((Part)stack.peek()).addHeader(tokens[0], tokens[1].trim());
            } catch (MessagingException me) {
                throw new Error(me);
            }
		}

		@Override
		public void field(Field field) throws MimeException {
			expect(Part.class);
            try {
                String[] tokens = ContentUtil.decode(field.getRaw()).split(":", 2);
                ((Part)stack.peek()).addHeader(tokens[0], tokens[1].trim());
            } catch (MessagingException me) {
                throw new Error(me);
            }
		}
    }
    
    /**
     * Parses the given string and returns an instance of the 
     * <code>Field</code> class. The type of the class returned depends on
     * the field name:
     * <table>
     *      <tr>
     *          <td><em>Field name</em></td><td><em>Class returned</em></td>
     *          <td>Content-Type</td><td>org.apache.james.mime4j.field.ContentTypeField</td>
     *          <td>other</td><td>org.apache.james.mime4j.field.UnstructuredField</td>
     *      </tr>
     * </table>
     * 
     * @param s the string to parse.
     * @return a <code>Field</code> instance.
     * @throws IllegalArgumentException on parse errors.
     */
    public static Field parse(final String raw) {
    	
        /*
         * Unfold the field.
         */
        final String unfolded = raw.replaceAll("\r|\n", "");
        
        /*
         * Split into name and value.
         */
        final Matcher fieldMatcher = fieldNamePattern.matcher(unfolded);
        if (!fieldMatcher.find()) {
            throw new IllegalArgumentException("Invalid field in string");
        }
        final String name = fieldMatcher.group(1);
        
        String body = unfolded.substring(fieldMatcher.end());
        if (body.length() > 0 && body.charAt(0) == ' ') {
            body = body.substring(1);
        }
        
        return parser.parse(name, body, ContentUtil.encode(raw));
    }
    
    private static final DefaultFieldParser parser = new DefaultFieldParser();
    private static final String FIELD_NAME_PATTERN = 
        "^([\\x21-\\x39\\x3b-\\x7e]+)[ \t]*:";
    private static final Pattern fieldNamePattern = 
        Pattern.compile(FIELD_NAME_PATTERN);
}
