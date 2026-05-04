package com.app.daymoya.global.mail;

import com.app.daymoya.global.exception.BizException;
import com.app.daymoya.global.properties.MailProperties;
import com.resend.Resend;
import com.resend.core.exception.ResendException;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

import static com.app.daymoya.global.exception.ErrorCode.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

  private final Resend resend;
  private final MailProperties mailProperties;

  private static final SecureRandom random = new SecureRandom();

  /** 인증코드 메일 전송 */
  public String sendMailCode(String toEmail) {

    String code = generateCode();

    // TODO: 도메인 생성 후 이메일 전송 처리
    /*
    CreateEmailOptions options = CreateEmailOptions.builder()
      .from(mailProperties.getFrom())
      .to(toEmail)
      .subject("[Daymoya] 인증 코드")
      .html("""
            <div>
              <h2>Daymoya 인증코드</h2>
              <p>아래 인증코드를 입력해주세요.</p>
              <h1 style="letter-spacing: 4px;">%s</h1>
              <p>인증코드는 5분간 유효합니다.</p>
            </div>
            """.formatted(code))
      .build();

    try {
      resend.emails().send(options);
    } catch (ResendException e) {
      log.error("sendMailCode Exception", e);
      throw new BizException(MAIL_SEND_FAIL);
    }
    */

    return code;
  }

  /** 인증코드 (6자리 숫자) 생성 */
  private String generateCode() {
    int number = random.nextInt(900000) + 100000; // 100000 ~ 999999
    return String.valueOf(number);
  }

}
