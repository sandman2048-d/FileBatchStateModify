package cn.sunline.batch.pojo;


public class KsysZdysql {

  private long sqlid;
  private String sqlname;
  private String sql;
  private String sqlparam;
  private String note;
  private String type;


  public long getSqlid() {
    return sqlid;
  }

  public void setSqlid(long sqlid) {
    this.sqlid = sqlid;
  }


  public String getSqlname() {
    return sqlname;
  }

  public void setSqlname(String sqlname) {
    this.sqlname = sqlname;
  }


  public String getSql() {
    return sql;
  }

  public void setSql(String sql) {
    this.sql = sql;
  }


  public String getSqlparam() {
    return sqlparam;
  }

  public void setSqlparam(String sqlparam) {
    this.sqlparam = sqlparam;
  }


  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
  }


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
