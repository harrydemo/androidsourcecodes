
public class decrypt {
    private static String str= "00,,4171,601,6711611031192,81,,84188,40,,8845,1531,919,,11,,11,109,70,,607,,10,,5571,5851,111,80,102,,809,,51211011,24120,115,154911451,116,,607,,15170,41,,65130,91,,20,,11,,001,916,,77451";
    public static String c(String s)
    {
        byte abyte0[];
        int i, j;
        abyte0 = s.getBytes();
        
        for ( i = 0, j = s.length() /2; i < j; i += 2 ){
            byte byte0 = abyte0[i];   //从前往后取字
            int l = (s.length() + -1) - i; //从后往前取字
            byte byte1 = abyte0[l];
            abyte0[i] = byte1;  //将后面的字写到前面
            abyte0[l] = byte0;  //将前面的字写到后面
        }
        String as[];
        String sss = new String(abyte0);
        as = (new String(abyte0)).split(",");
        String s1 = "";
        for ( i = 0; i < as.length; i += 2){
            String s2 = String.valueOf(s1);
            StringBuilder stringbuilder = new StringBuilder(s2);
            char c1 = (char)Integer.parseInt(as[i]);
            s1 = stringbuilder.append(c1).toString();
        }
        return s1;
    }    

    public static void main(String args[]){
        System.out.println(c(str));
    }

}
