package cn.wisesign.xamng.pojo;

import org.nutz.dao.entity.annotation.*;

@Table("xamng_steps")
public class UiStep {
    @Id
    private int stepId;

    @Column
    private String detail;

    @Column
    private String element;

    @Column
    private String operate;

    @Column
    private String target;

    @Column
    private int caseId;

    public int getCaseId() {
        return caseId;
    }

    public void setCaseId(int caseId) {
        this.caseId = caseId;
    }

    public int getStepId() {
        return stepId;
    }

    public void setStepId(int stepId) {
        this.stepId = stepId;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getElement() {
        return element;
    }

    public void setElement(String element) {
        this.element = element;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String opreate) {
        this.operate = opreate;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }


}
