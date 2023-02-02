package mscs.hms.service.impl;

import mscs.hms.entity.Inquiry;
import mscs.hms.repository.InquiryRepository;
import mscs.hms.service.InquiryService;
import org.springframework.beans.factory.annotation.Autowired;

public class InquiryServiceImpl implements InquiryService {

    @Autowired
    private InquiryRepository inquiryRepository;

    @Override
    public Inquiry save(Inquiry inquiry) {
        return inquiryRepository.save(inquiry);
    }

    @Override
    public Inquiry getById(Integer id) {
        return inquiryRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteById(Integer id) {
        inquiryRepository.deleteById(id);
    }

    @Override
    public Iterable<Inquiry> findAll() {
        return inquiryRepository.findAll();
    }
}