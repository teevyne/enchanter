package com.example.enchanter;

import com.twilio.Twilio;
import com.twilio.http.HttpMethod;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.messaging.v1.session.Webhook;
import com.twilio.twiml.TwiMLException;
import com.twilio.twiml.VoiceResponse;
import com.twilio.twiml.voice.Gather;
import com.twilio.twiml.voice.Say;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URI;

@Service
@Slf4j
@RequiredArgsConstructor
public class TwilioService {

    private final AppCredential appCredential;

    public void makeVoiceCall(String toNumber, String voiceCommandUrl) {

        Twilio.init(appCredential.ACCOUNTSID, appCredential.AUTHTOKEN);

        Call call = Call.creator(
                        new PhoneNumber(toNumber),
                        new PhoneNumber(appCredential.FROMNUMBER),
                        URI.create(voiceCommandUrl))
                .setMethod(HttpMethod.POST)
                .setStatusCallbackMethod(HttpMethod.POST)
                .setTwiML(null)
                .create();
    }

    public String getVoiceResponse(String messagePrompt) throws TwiMLException {

        VoiceResponse.Builder builder = new VoiceResponse.Builder();
        Say say = new Say.Builder(messagePrompt)
                .build();

        Gather gather = new Gather.Builder()
                .input(Gather.Input.SPEECH)
                .timeout(5)
                .action("/process_input")
                .method(Webhook.Method.GET)
                .say(say)
                .build();
        builder.gather(gather);

        VoiceResponse voiceResponse = builder.build();
        return voiceResponse.toXml();

    }
}