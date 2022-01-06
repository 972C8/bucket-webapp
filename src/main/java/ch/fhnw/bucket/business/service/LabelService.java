package ch.fhnw.bucket.business.service;

import ch.fhnw.bucket.data.domain.Label;
import ch.fhnw.bucket.data.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import java.util.List;

@Service
@Validated
public class LabelService {
    @Autowired
    private LabelRepository labelRepository;
    @Autowired
    private AvatarService avatarService;

    public Label saveLabel(@Valid Label label) {
        //Assign new label item to current user
        if (label.getAvatar() == null) {
            label.setAvatar(avatarService.getCurrentAvatar());
        }
        return labelRepository.save(label);
    }

    //TODO: Fix bug where labels are overwritten with parameter label instead of updated
    public Label updateLabel(@Valid Label label) throws Exception {
        //Check if label with given id is already present
        //Only labels with valid id are updated.
        if (!labelRepository.findById(label.getId()).isPresent()) {
            throw new Exception("No label with ID " + label.getId() + " found.");
        }
        return labelRepository.save(label);
    }

    public Label findLabelById(Long labelId) throws Exception {
        List<Label> labelList = labelRepository.findByIdAndAvatarId(labelId, avatarService.getCurrentAvatar().getId());
        if (labelList.isEmpty()) {
            throw new Exception("No label with ID " + labelId + " found.");
        }
        return labelList.get(0);
    }

    /*
    Delete label by id
     */
    public void deleteLabel(Long labelId) {
        labelRepository.deleteById(labelId);
    }

    /*
    Returns List<Label> of labels assigned to the given avatar
    */
    public List<Label> findAllLabels() {
        return labelRepository.findByAvatarId(avatarService.getCurrentAvatar().getId());
    }
}
