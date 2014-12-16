package cn.kenshin.office.word;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 利用已经做好的MHT单文件模板，读入后进行替换对应的内容
 */
public class MhtWordEditor {

    private String wordSource = null;

    /**
     * @return 当前Word文档
     */
    public String getWordSource() {
        return wordSource;
    }

    /**
     * @param path mht模版的Word文档路径
     * @throws java.io.IOException
     */
    public void open(String path) throws IOException {
        StringBuffer sourcecontent = new StringBuffer();
        InputStream ins = new FileInputStream(path);
        byte[] b = new byte[1024];
        int bytesRead = 0;
        while (true) {
            bytesRead = ins.read(b, 0, 1024);
            if (bytesRead == -1) {
                // 读取模板文件结束
                break;
            }
            sourcecontent.append(new String(b, 0, bytesRead));
        }
        ins.close();
        wordSource = sourcecontent.toString();
    }

    /**
     * @param markersign     欲被替换的字符串
     * @param replacecontent 用来替换的字符串
     */
    public void replaceStr(String markersign, String replacecontent) {
        if (wordSource == null) {
            return;
        }
        String rc = encode2HtmlUnicode(replacecontent);
        wordSource = wordSource.replace(markersign, rc);
    }

    /**
     * 把给定的str转换为10进制表示的unicode 只是用于mht模板的转码
     *
     * @param str 转换字符串
     * @return 10进制表示的unicode
     */
    private String encode2HtmlUnicode(String str) {
        if (str == null)
            return "";
        StringBuilder sb = new StringBuilder(str.length() * 2);
        for (int i = 0; i < str.length(); i++) {
            sb.append(encode2HtmlUnicode(str.charAt(i)));
        }
        return sb.toString();
    }

    private String encode2HtmlUnicode(char character) {
        if (character > 255) {
            return "&#" + (character & 0xffff) + ";";
        } else {
            return String.valueOf(character);
        }
    }

}

