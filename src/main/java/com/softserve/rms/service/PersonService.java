package com.softserve.rms.service;

import com.softserve.rms.dto.PersonDto;
import org.springframework.stereotype.Service;

@Service
public interface PersonService {
    boolean save(PersonDto person);

}
