package ats.algo.algomanager;

import java.io.UnsupportedEncodingException;
import java.util.zip.DataFormatException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

import org.springframework.jms.support.converter.MessageConversionException;
import org.springframework.jms.support.converter.MessageConverter;

import ats.algo.genericsupportfunctions.MQHelper;

public class ExternalMessageCompressor implements MessageConverter {

    @Override
    public Message toMessage(Object object, Session session) throws JMSException, MessageConversionException {
        try {
            return MQHelper.generateMessage(session, (String) object, true);
        } catch (UnsupportedEncodingException e) {
            throw new MessageConversionException("Exception generated in MQHelper.generateMessage" + e.getMessage());
        }
    }

    @Override
    public Object fromMessage(Message message) throws JMSException, MessageConversionException {
        try {
            return MQHelper.decodeMessage(message);
        } catch (UnsupportedEncodingException | DataFormatException e) {
            throw new MessageConversionException("Exception generated in MQHelper.decodeMessage" + e.getMessage());

        }
    }

}
