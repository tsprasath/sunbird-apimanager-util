package in.ekstep.am.builder;

public interface ConsumerDetails {
  void setGroups(String[] groups);

  void markFailure(String error, String errmsg);

  void markSuccess();
}
