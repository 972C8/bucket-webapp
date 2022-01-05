package ch.fhnw.acrm.business.service;

import ch.fhnw.acrm.data.domain.Label;
import ch.fhnw.acrm.data.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private AvatarService avatarService;

    public Label saveLabel(@Valid Label label) {
        //Assign new bucket item to current user
        if (label.getAvatar() == null) {
            label.setAvatar(avatarService.getCurrentAvatar());
        }
        return labelRepository.save(label);
    }
}
