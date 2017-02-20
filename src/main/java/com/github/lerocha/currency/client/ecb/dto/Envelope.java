package com.github.lerocha.currency.client.ecb.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

/**
 * Created by lerocha on 2/20/17.
 */
@JacksonXmlRootElement(namespace = "gesmes")
public class Envelope {
    @JacksonXmlProperty(namespace = "gesmes")
    private String subject;
    @JacksonXmlProperty(namespace = "gesmes", localName = "Sender")
    private Sender sender;
    @JacksonXmlProperty(localName = "Cube")
    private Cube cube;

    public Cube getCube() {
        return cube;
    }

    public void setCube(Cube cube) {
        this.cube = cube;
    }

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("subject=").append(subject)
                .append("; sender=").append(sender)
                .append("; cube=").append(cube)
                .toString();
    }
}
