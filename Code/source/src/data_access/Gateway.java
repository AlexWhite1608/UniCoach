package data_access;

import javax.annotation.Resource;
import domain_model.*;

public class Gateway {

    private Student student;

    @Resource
    private Professor professor;
}
