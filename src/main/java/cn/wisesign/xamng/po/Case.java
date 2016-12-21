package cn.wisesign.xamng.po;


import java.util.Date;

import org.nutz.dao.entity.annotation.*;

@Table("xamng_case")
public class Case {
	@Id private int id;
	@Name @Column private String name;
	@Column("belongProject") private String belongProject;
	@Column("script") @ColDefine(customType="LONGTEXT", type=ColType.VARCHAR) private String script;
	@Column("createdate") private Date createdate;
	@Column("author") private String author;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getBelongProject() {
		return belongProject;
	}
	public void setBelongProject(String belongProject) {
		this.belongProject = belongProject;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public Date getCreatedate() {
		return createdate;
	}
	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}

}
