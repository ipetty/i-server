package net.ipetty.activity.mq;

import javax.annotation.Resource;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import net.ipetty.activity.domain.Activity;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

/**
 * ActivityMQMessageSender
 * 
 * @author luocanfeng
 * @date 2014年7月8日
 */
@Component
public class ActivityMQMessageSender {

	@Resource
	private JmsTemplate jmsTemplate;

	public void publish(Activity activity) {
		jmsTemplate.send(new ActivityMessageCreator(activity));
	}

	public class ActivityMessageCreator implements MessageCreator {
		private Activity activity;

		public ActivityMessageCreator(Activity activity) {
			super();
			this.activity = activity;
		}

		@Override
		public Message createMessage(Session session) throws JMSException {
			ObjectMessage message = session.createObjectMessage(activity);
			return message;
		}
	}

}
