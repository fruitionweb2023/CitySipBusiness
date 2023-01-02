package com.direct2web.citysip.Utils;


import com.direct2web.citysip.Model.RestaurentModels.Cuisines.DishType;

import java.security.Key;
import java.util.ArrayList;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class WS_URL_PARAMS {


    public static String service_selection_text = "";

    public static ArrayList<DishType> dishTypeItem=new ArrayList<>();

    public static String status = "";

    public static String access_key = "90336";
    public static String issuer = "eKart";
    public static String subject = "eKart Authentication";

    public static String JWT_KEY = "replace_with_your_strong_jwt_secret_key";

    //public static String header = "Bearer " + WS_URL_PARAMS.createJWT();



    public static String createJWT(String issuer, String subject) {
        try {
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            long nowMillis = System.currentTimeMillis();
            Date now = new Date(nowMillis);
            byte[] apiKeySecretBytes = JWT_KEY.getBytes();
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
            JwtBuilder builder = Jwts.builder()
                    .setIssuedAt(now)
                    .setSubject(subject)
                    .setIssuer(issuer)
                    .signWith(signatureAlgorithm, signingKey);

            return builder.compact();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
