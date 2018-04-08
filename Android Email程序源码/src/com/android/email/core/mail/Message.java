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

package com.android.email.core.mail;

import java.util.Date;
import java.util.HashSet;

public abstract class Message implements Part, Body {
    public enum RecipientType {
        TO, CC, BCC,
    }

    protected String mUid;

//    protected HashSet<Flag> mFlags = new HashSet<Flag>();

    protected Date mInternalDate;

//    protected Folder mFolder;

    public String getUid() {
        return mUid;
    }

    public void setUid(String uid) {
        this.mUid = uid;
    }

//    public Folder getFolder() {
//        return mFolder;
//    }

    public abstract String getSubject() throws MessagingException;

    public abstract void setSubject(String subject) throws MessagingException;

    public Date getInternalDate() {
        return mInternalDate;
    }

    public void setInternalDate(Date internalDate) {
        this.mInternalDate = internalDate;
    }

    public abstract Date getReceivedDate() throws MessagingException;

    public abstract Date getSentDate() throws MessagingException;

    public abstract void setSentDate(Date sentDate) throws MessagingException;

    public abstract Address[] getRecipients(RecipientType type) throws MessagingException;

    public abstract void setRecipients(RecipientType type, Address[] addresses)
            throws MessagingException;

    public void setRecipient(RecipientType type, Address address) throws MessagingException {
        setRecipients(type, new Address[] {
            address
        });
    }

    public abstract Address[] getFrom() throws MessagingException;

    public abstract void setFrom(Address from) throws MessagingException;

    public abstract Address[] getReplyTo() throws MessagingException;

    public abstract void setReplyTo(Address[] from) throws MessagingException;

    public abstract Body getBody() throws MessagingException;

    public abstract String getContentType() throws MessagingException;

    public abstract void addHeader(String name, String value) throws MessagingException;

    public abstract void setHeader(String name, String value) throws MessagingException;

    public abstract String[] getHeader(String name) throws MessagingException;

    public abstract void removeHeader(String name) throws MessagingException;

    public abstract void setBody(Body body) throws MessagingException;

    public boolean isMimeType(String mimeType) throws MessagingException {
        return getContentType().startsWith(mimeType);
    }

//    /*
//     * TODO Refactor Flags at some point to be able to store user defined flags. 
//     */
//    public Flag[] getFlags() {
//        return mFlags.toArray(new Flag[] {});
//    }
//
//    public void setFlag(Flag flag, boolean set) throws MessagingException {
//        if (set) {
//            mFlags.add(flag);
//        } else {
//            mFlags.remove(flag);
//        }
//    }
//
//    /**
//     * This method calls setFlag(Flag, boolean)
//     * @param flags
//     * @param set
//     */
//    public void setFlags(Flag[] flags, boolean set) throws MessagingException {
//        for (Flag flag : flags) {
//            setFlag(flag, set);
//        }
//    }
//
//    public boolean isSet(Flag flag) {
//        return mFlags.contains(flag);
//    }

    public abstract void saveChanges() throws MessagingException;
}
