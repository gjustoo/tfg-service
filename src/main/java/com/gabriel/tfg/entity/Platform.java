package com.gabriel.tfg.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

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
public class Platform extends GenericEntity<Platform> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String url;

    private String apiKey;

    private String apiKeySecret;

    private String bearerToken;

    private String accessToken;

    private String accessTokenSecret;

    private String clientId;

    private String clientSecret;

    private String iconUrl;

    public JSONObject getJson() {
        JSONObject result = new JSONObject();
        result.put("name", this.name);
        result.put("url", this.url);
        result.put("icon_url", this.iconUrl);

        return result;
    }

}
