package data_access;

import javax.annotation.Resource;
import domain_model.*;

public class Gateway {



    @Resource
    private Student student;

    @Resource
    private Professor professor;
}
