package ats.algo.genericsupportfunctions;

import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.jms.BytesMessage;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

public class MQHelper {

    /**
     * creates a message of type TextMessage or BytesMessage, depending on whether compression is to be applied
     * 
     * @param session
     * @param json
     * @param compressMessage
     * @return
     * @throws JMSException
     * @throws UnsupportedEncodingException
     */
    public static Message generateMessage(Session session, String json, boolean compressMessage)
                    throws JMSException, UnsupportedEncodingException {
        Message message = null;
        if (compressMessage) {
            byte[] input = null;
            input = json.getBytes("UTF-8");
            byte[] buffer = new byte[input.length];
            Deflater compresser = new Deflater();
            compresser.setInput(input);
            compresser.finish();
            int compressedDataLength = compresser.deflate(buffer);
            compresser.end();
            message = session.createBytesMessage();
            ((BytesMessage) message).writeBytes(buffer, 0, compressedDataLength);
        } else {
            message = session.createTextMessage();
            ((TextMessage) message).setText(json);
        }
        return message;
    }

    public static final int INFLATER_BUFFER_SIZE = 100000;

    public static String decodeMessage(Message message)
                    throws JMSException, DataFormatException, UnsupportedEncodingException {
        String string = null;
        if (!isCompressed(message)) {
            string = ((TextMessage) message).getText();
        } else {
            int compressedMsgLength = (int) ((BytesMessage) message).getBodyLength();
            byte[] data = new byte[compressedMsgLength];
            ((BytesMessage) message).readBytes(data, compressedMsgLength);
            Inflater inflater = new Inflater();
            inflater.setInput(data);
            byte[] buffer = new byte[INFLATER_BUFFER_SIZE];
            StringBuilder sb = new StringBuilder();
            int resultLength = 0;
            do {
                resultLength = inflater.inflate(buffer, 0, INFLATER_BUFFER_SIZE);
                sb.append(new String(buffer, 0, resultLength, "UTF-8"));
            } while (resultLength == INFLATER_BUFFER_SIZE);
            inflater.end();
            string = sb.toString();

        }
        return string;
    }

    public static boolean isCompressed(Message message) {
        return message instanceof BytesMessage;
    }

    public static int getMessageLength(Message message) {
        int messageLength = 0;;
        try {
            if (message instanceof BytesMessage)
                messageLength = (int) ((BytesMessage) message).getBodyLength();
            else
                messageLength = (int) ((TextMessage) message).getText().length();
        } catch (JMSException e) {
            /*
             * do nothing
             */
        }
        return messageLength;
    }

}
