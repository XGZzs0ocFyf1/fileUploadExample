package a.v.g.wordApp.service;

import a.v.g.wordApp.exceptions.CodeNotFoundException;
import a.v.g.wordApp.model.AuthCode;
import a.v.g.wordApp.model.sec.User;

public interface AuthCodeService {
    AuthCode generateAuthCode(User user);
    void submitAuthCode(String authCode) throws CodeNotFoundException;
    void disableAuthCode(String authCode);
}
