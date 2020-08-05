package com.ssafy.study.common.notification;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.nimbusds.jose.Payload;
import com.ssafy.study.common.model.Attachment;
import com.ssafy.study.common.model.Attachments;

import lombok.ToString;

@Component
@ToString
public class MatterMostSender {
	private Logger log = LoggerFactory.getLogger(MatterMostSender.class);

	private boolean mmEnabled = true;
	private String webhookUrl = "https://meeting.ssafy.com/hooks/ou9oee6tq3ytuyzwnid1gmzkar";

	public void sendMessage(Attachment attach) {
		if (mmEnabled) {
			try {
				List<Attachment> list = new ArrayList<Attachment>();
				list.add(attach);
				Attachments attachments = new Attachments(list);

				String payload = new Gson().toJson(attachments);

				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.set("Content-type", MediaType.APPLICATION_JSON_VALUE);

				HttpEntity<String> entity = new HttpEntity<>(payload, headers);
				restTemplate.postForEntity(webhookUrl, entity, String.class);
			} catch (Exception e) {
				log.error("ERROR!! While send MatterMost Message : " + e.getMessage());
			}
		}
	}

}
