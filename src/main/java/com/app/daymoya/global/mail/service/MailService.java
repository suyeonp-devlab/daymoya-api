package com.app.daymoya.global.mail.service;

import com.app.daymoya.global.properties.MailProperties;
import com.resend.Resend;
import com.resend.services.emails.model.CreateEmailOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

  private final Resend resend;
  private final MailProperties mailProperties;

  private static final SecureRandom random = new SecureRandom();

  public String sendMailCode(String toEmail) {

    String code = generateCode();

    CreateEmailOptions options = CreateEmailOptions.builder()
      .from(mailProperties.getFrom())
      .to(toEmail)
      .subject("[Daymoya] 이메일 인증 코드 테스트")
      .html("""
            <div>
              <h2>이메일 인증코드</h2>
              <p>아래 인증코드를 입력해주세요.</p>
              <h1 style="letter-spacing: 4px;">%s</h1>
              <p>인증코드는 5분간 유효합니다.</p>
            </div>
            """.formatted(code))
      .build();

//    TODO: 도메인 생성 후 이메일 전송 처리
//    try {
//      resend.emails().send(options);
//    } catch (ResendException e) {
//      log.error("sendMailCode Exception", e);
//      throw new CustomException(MAIL_SEND_FAIL);
//    }

    return code;
  }

  private String generateCode() {
    int number = random.nextInt(900000) + 100000; // 100000 ~ 999999
    return String.valueOf(number);
  }

}
