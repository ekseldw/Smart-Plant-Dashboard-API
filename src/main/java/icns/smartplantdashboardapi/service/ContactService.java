package icns.smartplantdashboardapi.service;

import icns.smartplantdashboardapi.advice.exception.SensorPosNotFoundException;
import icns.smartplantdashboardapi.domain.Contact;
import icns.smartplantdashboardapi.domain.SensorPos;
import icns.smartplantdashboardapi.dto.contact.ContactRequest;
import icns.smartplantdashboardapi.dto.contact.ContactResponse;
import icns.smartplantdashboardapi.repository.ContactRepository;
import icns.smartplantdashboardapi.repository.SensorPosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContactService {
    private final ContactRepository contactRepository;
    private final SensorPosRepository sensorPosRepository;


    @Transactional
    public Long save(ContactRequest contactRequest){
        SensorPos sensorPos = sensorPosRepository.findById(contactRequest.getPosId()).orElseThrow(SensorPosNotFoundException::new);
        Contact saved = contactRepository.save(contactRequest.toEntity(sensorPos));
        return saved.getId();
    }

    @Transactional
    public ContactResponse updateById(Long id, ContactRequest contactRequest){
        Contact contact = contactRepository.findById(id).get();
        SensorPos sensorPos = sensorPosRepository.findById(contactRequest.getPosId()).get();
        contact.update(contactRequest, sensorPos);
        return new ContactResponse(contact);

    }
    
    @Transactional(readOnly = true)
    public Page<ContactResponse> find(Long posId, Pageable pageable){
        Page<ContactResponse> contactList;
        if(posId == null){
            contactList = contactRepository.findAll(pageable).map(ContactResponse::new);
        }else{
            contactList = contactRepository.findBySsPos_PosId(posId, pageable).map(ContactResponse::new);
        }
        return contactList;
    }
    /*
    @Transactional(readOnly = true)
    public List<Contact> find(Long posId){
        List<Contact> contactList;
        if(posId == null){
            contactList = contactRepository.findAll();
        }else{
            contactList = contactRepository.findBySsPos_PosId(posId);
        }
        return contactList;
    }
     */
    
    


    @Transactional
    public Long deleteById(Long id){
        Long deleted = contactRepository.findById(id).get().getId();
        contactRepository.deleteById(id);
        return deleted;
    }


}
