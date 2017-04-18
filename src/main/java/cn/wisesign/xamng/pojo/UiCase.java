package cn.wisesign.xamng.pojo;


import java.util.Date;
import java.util.List;

import org.nutz.dao.entity.annotation.*;

@Table("xamng_case")
public class UiCase {

	@Id
    private int caseId;

	@Column
    private String caseDetail;

	@Many(field="caseId")
    private List<UiStep> steps;

	@Column("createdate")
    private Date createdate;

	public int getCaseId() {
		return caseId;
	}

	public void setCaseId(int caseId) {
		this.caseId = caseId;
	}

	public String getCaseDetail() {
		return caseDetail;
	}

	public void setCaseDetail(String caseDetail) {
		this.caseDetail = caseDetail;
	}

	public List<UiStep> getSteps() {
		return steps;
	}

	public void setSteps(List<UiStep> steps) {
		this.steps = steps;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}


}
