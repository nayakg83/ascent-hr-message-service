package com.ascent.hr.ascenthrmessageservice.model;

import lombok.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author gopal
 * @since 1/9/19
 */

@Getter
@Entity
@NoArgsConstructor
public class MessageQueue {

    @Id
    @Column(length = 64)
    private String id = UUID.randomUUID().toString();

    @Column(unique = true)
    private String name;

    private int size;

    @Column(unique = true)
    private String exchangeName;

    @Setter
    @OneToMany(fetch = FetchType.EAGER, orphanRemoval = true)
    @Cascade({CascadeType.ALL, CascadeType.SAVE_UPDATE})
    @JoinTable(name="message_queue_message_mapping", joinColumns = @JoinColumn(name = "message_queue_id"),
            inverseJoinColumns = @JoinColumn(name = "message_id"))
    private List<Message> messages = new ArrayList<>();

    public void name(String name){
        this.name = name;
    }

    public void exchangeName(String exchangeName){
        this.exchangeName = exchangeName;
    }

    public void size(){
        this.size = this.messages.size();
    }

}

