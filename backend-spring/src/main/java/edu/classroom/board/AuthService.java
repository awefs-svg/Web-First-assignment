package edu.classroom.board;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Value; // @value: Spring Boot 설정 값을 가져옴

import io.jsonwebtoken.Jwts; // JWT를 생성하기 위한 기능을 가져오는 코드 ( Jwts.buildder() )
import io.jsonwebtoken.security.Keys; // Keys : HS256 서명에 사용할 키를 만듦
// JWT 서명에 사용할 비밀 키를 만드는 기능을 가져옴

import javax.crypto.SecretKey; // 비밀 키의 자료형을 사용하기 위한 import
// SecretKey : 암호화 관련 비밀 키를 나타내는 자료형
import java.time.Instant; // 현재 시각을 가져오고 만료 시각을 계산하기 위한 import
import java.util.Date; // 시간을 다루는 클래스로 JWT 라이브러리의 issueAt(), expriation()에 전달할 때 사용하기 위한 import
import java.util.HexFormat; // HexFormat : 64자리 16진수를 32바이트로 변환하는 기능

// BE-02
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;


@Service
public class AuthService {
  //BE-01
  private final SecretKey key;
  private final String issuer;
  private final String audience;
  private final long ttl;
  // BD-02
  private final Users users;
  public AuthService( // AuthService  생성자
          // Spring이 AuthService 객체를 생성할 때 JWT 설정값을 가져오도록 만듦
          // @Value : Spring에서 설정값을 가져오는 Annotation ( 등록한 서버 비밀 키 등을 Java 코드에서 사용할 수 있도록 전달 )
          @Value("${app.jwt.secret}") String secret,
          @Value("${app.jwt.issuer}") String issuer,
          @Value("${app.jwt.audience}") String audience,
          @Value("${app.jwt.ttl}") long ttl
          ,Users users
  ) {
    // 서버 비밀 키 검사
    if (secret == null || !secret.matches("^[0-9a-fA-F]{64}$")) {
      throw new IllegalStateException(
              "JWT_SECRET은 64자리 16진수여야 합니다."
      );
    }
    // 제공된 예시 키 검사
    // equalsIgnoreCase() : 두 문자열을 비교하는 메서드
    if (secret.equalsIgnoreCase("REPLACE_WITH_RANDOM_64_HEX_CHARACTERS")) {
      throw new IllegalStateException("제공된 예시 키는 사용할 수 없습니다.");
    }
    //TTL 검사
    if (ttl < 1){
      throw new IllegalStateException("JWT 만료 시간은 1초 이상이어야 합니다.");
    }

    // Buffer.from(secret, 'hex')에 대응
    byte[] keyBytes = HexFormat.of().parseHex(secret);
//=>  HexFormat.of() # 16진수 형식으로 된 데이터를 처리하는 객체를 가져와
//=> .parseHex(secret) # 16진수 문자열 -> 바이트 배열로 변환

    this.key = Keys.hmacShaKeyFor(keyBytes); // 등록한 키 문자열을 32바이트로 변환 -> JWT 서명에 사용할 수 있는 키 객체로 만들어 줌
//=> 바이트 배열 -> HMAC 서명에 사용할 수 있는 SecretKey 객체로 만드는 것
//=> key.hmacShaKeyFor() : JJWT 라이브러리에서 제공하는 메서드

    this.issuer = issuer;
    this.audience = audience;
    this.ttl = ttl;

    this.users = users;
  }
  // JJWT 의존성은 pom.xml에 포함되어 있습니다. app.jwt.* 설정은 application.properties 참조.
  // 실제 JWT 발급 부분
  public String issueToken(Users.PublicUser user) {
    // Users.PublicUser : 사용자 정보를 담는 자료형
    // user : 그 정보를 저장하는 변수
    // TODO BE-01: 서버 키로 HS256 JWT 발급. sub, exp, iss, aud 필수.

    Instant now = Instant.now(); // 현재 시각 가져오기
    Instant expiresAt = now.plusSeconds(ttl); // JWT 만료 시각 계산하기
    // 현재 시각에 TTL만큼 초를 더해 만료 시각을 계산하는 코드

    return Jwts.builder().subject(String.valueOf(user.id())).issuer(issuer)
            .audience().add(audience).and().issuedAt(Date.from(now))
            .expiration(Date.from(expiresAt))
            .signWith(key, Jwts.SIG.HS256).compact();
    // Jwts.builder () : JWT 만들기 시작 / 빌더(builder) : 필요한 설정을 하나씩 추가해서 최종 객체를 완성하는 방식
    // subject(String.valueOf(user.id())) : JWT의 sub 값을 설정 ( 토큰의 대상이 되는 사용자 )
    //  ==> String.valueOf() : 숫자를 문자열로 변환하는 메서드
    // .issuer(issuer) : 토큰을 발급한 주체 ( 현재 기본 설정 : webservice-class )
    // audience().add(audience).and() : 사용 대상 설정 ( aud = 이 토큰을 사용할 수 있는 대상 서비스 )
    //   (1) .audience() : JWT의 Audience 설정을 시작
    //   (2) .add(audience) : 사용 대상 값을 추가 / (3).and() : Audience 설정을 마치고 원래 JWT 빌더로 돌아감
    // issuedAt() : JWT 발급 시각 설정 * Date 객체를 전달 받음 *
    // expiration() : JWT 만료 시각 설정
    // signWith() : JWT에 서명을 추가하는 메서드
    //    => Jwts.SIG.HS256 : HS256이라는 서명 알고리즘을 사용
    // compact() : 완성된 JWT 문자열 반환

  //  throw new ApiError(501, "NOT_IMPLEMENTED", "JWT 발급을 구현하세요. BE-01");
  }

  public long currentUserId(HttpServletRequest request) {
    // TODO BE-02: Bearer 추출 → 서명/알고리즘/만료/클레임 검증 → 실제 사용자 확인.
    // 완성 후 아래 데모 ID 반환을 제거하세요. 토큰 없음/변조/만료는 401입니다.

    // HTTP요청에서 Authorization 이라는 이름의 헤더를 읽는 메서드
    String header = request.getHeader("Authorization");
    // 토큰 유무 확인
    if(header ==null || header.isBlank()){
      throw new ApiError(401, "TOKEN_REQUIRED","토큰이 필요합니다.");
    }
    // Baerer 형식 확인
    // regionMatches : 문자열의 처음 7글자가 Bearer인지 비교하는 메서드
    if(!header.regionMatches(true,0,"Bearer ",0,7) || header.substring(7).isBlank()){
      throw new ApiError(401, "TOKEN_REQUIRED", "Bearer 토큰을 보내세요.");
    }
    // JWT만 추출
    String token = header.substring(7).trim(); //trim() : 문자열 양끝 공백 제거
    try{
      var jwt = Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
      // parser() : JWT를 읽고 검증하기 위한 parser 생성
      // verifyWith(key) : BE-01에서 사용한 비밀 키 -> 검증 키로 지정
      // build() : 설정된 JWT parser 생성
      // parseSignedClaims(token) : JWT 서명을 검증 & 클레임을 읽기, 만료된 토큰 등은 예외 발생

      // Claims : JWT 내부에 저장된 클레임 정보를 담는 자료형
      // getPayload() : 검증된 JWT에 payload를 가져오는 메서드
      Claims claims = jwt.getPayload();

      if (!"HS256".equals(jwt.getHeader().getAlgorithm())
              ||!issuer.equals(claims.getIssuer())
              || claims.getAudience() == null
              || !claims.getAudience().contains(audience)
              || claims.getIssuedAt() == null || claims.getExpiration() == null
              || claims.getSubject()  == null
              || !claims.getSubject().matches("[1-9][0-9]*")) // 사용자 ID가 0이 아닌 양의 정수 형태인지 검사하는 정규표현식
      {
        throw new ApiError(401,"INVALID_TOKEN", "토큰 정보가 올바르지 않습니다.");
      }
      if (claims.getIssuedAt().after(new Date())) {
        throw new ApiError(401, "INVALID_TOKEN","발급 시각이 올바르지 않습니다.");
      }

      long userId;
      try {
        userId = Long.parseLong(claims.getSubject()); // 사용자 ID를 숫자로 변환
      }catch(NumberFormatException e){
        throw new ApiError(401,"INVALID_TOKEN", "사용자 ID가 올바르지 않습니다.");
      }
      if (users.find(userId) == null){
        throw new ApiError(401,"INVALID_TOKEN", "사용자를 확인할 수 없습니다.");
      }
      return userId;
    } catch (ExpiredJwtException e){
      throw new ApiError(401,"TOKEN_EXPIRED", "토큰이 만료되었습니다.");
    } catch (JwtException | IllegalArgumentException e){
      throw new ApiError(401,"INVALID_TOKEN","유효하지 않은 토큰입니다.");
    }


//    return 1L;
  }

  public void assertOwner(long userId, long authorId) {
    // TODO BE-03: 다른 작성자이면 403 FORBIDDEN. 현재는 검사하지 않습니다.
    if (userId != authorId){
      throw new ApiError(403, "FORBIDDEN","본인이 작성한 게시글만 수정하거나 삭제할 수 있습니다.");
    }
  }
}
