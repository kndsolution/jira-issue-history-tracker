
## 개발 환경 설정

- os : linux (ubuntu 20.04)  or window 
- jdk 1
- jira 9.12.x

## Install jdk 
- 공식 문서에서는 jdk 8을 권장하나 jdk 11을 설치 [ JDK 설치 가이드 ](https://confluence.atlassian.com/adminjiraserver/installing-java-938846828.html)
- 버전 확인
```bash
java --version
```
- 홈 설정 확인
```bash
echo $JAVA_HOME
```
## Install atlassian-sdk 
 - 다음 가이드 문서를 따라 [공식 가이드 문서](https://developer.atlassian.com/server/framework/atlassian-sdk/downloads/) atlassian-sdk를 설치한다 
 - 버전 확인
```bash
atlas-mvn -v 
```

## Source clone & build
 - 소스를 가져온다 
```bash
 git clone https://github.com/catmasterlim/jira-plugin-base.git
```
- 디렉토리로 이동 후 빌드
```bash
cd jira-plugin-base
atlas-package
```

## 테스트 서버 기동 및 확인
- 테스트 서버를 실행
```bash
atlas-run
```
- 다음과 유사한 메세지가 나올때 까지 대기
```bash
[INFO] [talledLocalContainer] Tomcat 9.x started on port [2990]
[INFO] jira started successfully in 55s at http://localhost:2990/jira
[INFO] Type Ctrl-C to shutdown gracefully
```
- 이후 jira 접속해서 확인
	- 접속 주소 위에 명시된 http://localhost:2990/jira
	- 접속 id/pass : admin/admin
	- 기본설정으로 quickload 가 적용되어 있기 때문에 소스 변경 후 atlas-package하면 자동으로 테스트 서버에 적용된다.