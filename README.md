# LH Digital Twin Platform

<strong>LH 도시개발 업무 의사결정을 지원할 수 있는 디지털트윈 플랫폼입니다.</strong>


## 사업 개요   
### 1. 사업 일반
   - 사업명 : LH 디지털트윈 플랫폼 1단계 구축
   - 사업기간 : ~ 2021.02.27
 
### 2. 사업 범위
   - 디지털트윈 기반플랫폼 구축
    - 지구계획 수립지원을 위한 시뮬레이션 서비스 구축
        - 도시계획 / 경관 / 가로시설물 분석, 시뮬레이션 서비스 설계 및 구현
        - 미세먼지, 실내점유자 모니터링, 소음지도 서비스 설계 및 구현
    - 디지털트윈 활용서비스 컨설팅

### 3. 목표
#### 스마트시티의 활용 기반이 되는 도시 공간 3차원 데이터의 가시화 기능을  포함하는 디지털트윈 플랫폼을 개발하고, 이를 기반으로 대상 구역의 파일럿 서비스를 개발
   - 플랫폼 및 파일럿 서비스를 통한 개발 타당성 검증 및 사용자 참여 기반 마련 

## Features
- 2차원 공간정보 관리 기능
- 3차원 데이터 포맷 지원 기능
- 3차원 데이터 관리기능
- 자동화된 2차원/3차원 공간정보 관리 기능
- 3차원 가시화 기능
- 3차원 데이터 운영 기능
- 시뮬레이션 서비스 연동

## Development Environment
- JAVA(OpenJDK) 11.0.2
- Spring Boot 2.3.0
- PostgreSQL 12
- PostGIS 3.0
- Gradle 6.5.0
- mybatis
- lombok

## Project Composition
- lhdt-admin : 플랫폼(lhdt) 관리자     
- lhdt-converter : 3차원 공간정보 자동화 관리
- lhdt-user : 2차원/3차원  공간데이터 조회, 시뮬레이션 연동 등
- common : 암호화(보안), 통계모듈 등 공통 기능 관리
- doc : database schema, 개발 문서
- html : html 디자인 파일 (npm init으로 생성)

## Getting Started

### 1. Install
#### [java](https://jdk.java.net/archive/)
- OpenJDK 11.0.2 (build 11.0.2+9) : 11버전 설치

#### [eclipse](https://www.eclipse.org/downloads/download.php?file=/oomph/epp/2019-12/R/eclipse-inst-win64.exe)
- Eclipse IDE 2019-12 (2019-12(4.14.0) 버전 이상 설치)<br>
- Eclipse 설정 - STS(Spring Tools) 설정 <br>
  Help → Eclipse Marketplace → 'STS' 검색후, Spring Tools 4 설치
- Eclipse를 실행 후 Project Import <br>
  File → import → Gradle → Existing Gradle Project

#### [PostgreSQL](https://www.enterprisedb.com/downloads/postgres-postgresql-downloads)
- PostgreSQL11.6 버전으로 설정
- 설치경로 C:/PostgreSQL/11 <br>
  doc/database/doc/database/ 참조 
- [PostGIS](https://postgis.net/) 최신 SQL 버전으로 설정
- 설치경로 C:/PostGIS

#### [GDAL](https://trac.osgeo.org/osgeo4w/)
- GDAL을 설치하기 위해서 OSGeo4W(FOSSGIS for Windows)를 설치
- 시스템 변수 추가 <br>
  Path) C:\OSGeo4W64\bin 

#### [gradle](https://gradle.org/docs/)
- 설치경로 C:/gradle
- 시스템 변수 추가 <br> 
  Path) C:\gradle\gradle-5.6.3 
- eclipse BuildShip Gradle Plugin을 이용하여 gradle을 사용할 수 있습니다.

#### [lombok](https://projectlombok.org/)
- 설치한 뒤에 다운로드 폴더 이동 후 실행
- eclipse 설치 위치 [Specify location..]를 검색해서 'eclipse.exe' 파일을 선택합니다.
- install/update 클릭합니다.

  
### 2. DB 생성 및 초기 데이터 등록
- Database & Extensions
   - ndtp 데이터베이스를 생성합니다.
       한글 정렬을 위해 데이터베이스를 다음과 같이 설정합니다.
     <pre><code>Name:ndtp, Encoding:UTF-8, Template:template0, Collation:C, Character type:C, Connection Limit:-1</code></pre>
   - psql(SQL Shell) 혹은 pgAdmin에서 Extensions를 실행합니다.
     <pre><code>CREATE EXTENSION postgis</code></pre>
     PosGIS Extensions이 성공적으로 끝나면 데이터베이스 생성 및 초기 데이더 등록 후 spatial_ref_sys라는 테이블이 자동 생성됩니다.

- 데이터 등록
      
### 3. Execution
- ndtp-admin project spring boot 실행 <br>
  url : http://localhost(:port)/
<pre><code>/ndtp-admin/src/main/java/ndtp/NdtpAdminApplication.java</code></pre>

## License

<br><br>

