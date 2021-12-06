package icns.smartplantdashboardapi.service;


import icns.smartplantdashboardapi.domain.Contact;
import icns.smartplantdashboardapi.domain.SopDetail;
import icns.smartplantdashboardapi.domain.SopDetailContent;
import icns.smartplantdashboardapi.domain.SopMessageLog;
import icns.smartplantdashboardapi.repository.ContactRepository;
import icns.smartplantdashboardapi.repository.SopDetailContentRepository;
import icns.smartplantdashboardapi.repository.SopDetailRepository;
import icns.smartplantdashboardapi.repository.SopMessageLogRepository;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SopMessageService {

    private final ContactRepository contactRepository;
    private final SopMessageLogRepository sopMessageLogRepository;
    private final SopDetailContentRepository sopDetailContentRepository;
    private final SopDetailRepository sopDetailRepository;


    @Value("${icns.app.coolsms.apikey}")
    private String apiKey;

    @Value("${icns.app.coolsms.apisecret}")
    private String apiSecret;

    @Value("${icns.app.coolsms.phone}")
    private String phone;


    public List<String> sendMessage(String name, Long contentId){
        SopDetailContent sopDetailContent = sopDetailContentRepository.findById(contentId).get();
        List<Contact> contactList = contactRepository.findBySsPos_PosIdAndLevelLessThanEqual(sopDetailContent.getSsPos().getPosId(), sopDetailContent.getSopDetail().getLevel());

        List<String> phoneList = new ArrayList<>();

        for(Contact contact : contactList){
            String api_key = apiKey;
            String api_secret = apiSecret;
            Message coolsms = new Message(api_key, api_secret);

            phoneList.add(contact.getPhone());


            HashMap<String, String> params = new HashMap<String, String>();

            params.put("to", contact.getPhone());
            params.put("from", phone);
            params.put("type", "SMS");
            params.put("text", sopDetailContent.getInfo());
            params.put("app_version", "test app 1.2");



            try {
                JSONObject obj = (JSONObject) coolsms.send(params);
                System.out.println(obj.toString());
                SopMessageLog sopMessageLog = SopMessageLog.builder()
                        .send(true)
                        .sender(name)
                        .receiver(contact.getName())
                        .text(sopDetailContent.getInfo())
                        .build();
                sopMessageLogRepository.save(sopMessageLog);

            } catch (CoolsmsException e) {
                System.out.println(e.getMessage());
                System.out.println(e.getCode());
                SopMessageLog sopMessageLog = SopMessageLog.builder()
                        .send(false)
                        .sender(name)
                        .receiver(contact.getName())
                        .text(sopDetailContent.getInfo())
                        .build();
                sopMessageLogRepository.save(sopMessageLog);
            }
        }


        return phoneList;
    }


    @Transactional(readOnly = true)
    public Page<SopMessageLog> findAll(Pageable pageable){
        Page<SopMessageLog> messageLogList = sopMessageLogRepository.findAll(pageable);
        return messageLogList;
    }
}
