package com.gabriel.tfg.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import kong.unirest.json.JSONObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
@Entity
public class FeedNode extends GenericEntity<FeedNode> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String uid;

    private String name;

    @ManyToOne
    private Platform platform;

    public JSONObject getJSON() {
        JSONObject result = new JSONObject();
        result.put("id", this.id);
        result.put("uid", this.uid);
        result.put("name", this.name);
        result.put("platform", this.platform.getJson());

        return result;

    }

}
