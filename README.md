# API Otomasyon Projesi

Bu proje, REST API test otomasyonu için hazırlanmış bir test projesidir. WireMock kullanılarak mock API requestler ve responselar oluşturulmuş ve bu API'lar üzerinde çeşitli test senaryoları gerçekleştirilmiştir.

## Ön Gereksinimler

- Java 11 veya üzeri
- Maven 3.6.3 veya üzeri
- Git

## Kurulum

1. Projeyi klonlayın:
   ```bash
   git clone https://github.com/berkaneris2405/api-automation-case-study.git
   cd api-automation-case-study
   ```

2. Proje bağımlılıklarını yükleyin ve kodu formatlayın:
   ```bash
   mvn clean
   mvn spring-javaformat:apply
   ```

## Testleri Çalıştırma

Tüm testleri çalıştırmak için:
```bash
mvn clean test
```

## Proje Yapısı

```
src/
├── main/
│   ├── java/
│   │   └── mockapi/
│   │       ├── controller/    # API endpoint'leri
│   │       ├── models/        # Request/Response modelleri
│   │       └── service/       # WireMock servisleri
│   └── resources/             # Konfigürasyon dosyaları
└── test/
    └── java/
        └── mockapi/
            ├── BaseTest.java  # Temel test sınıfı
            └── MockApiTest.java # Test senaryoları
```

## Kullanılan Teknolojiler

- **Test Framework**: JUnit 5
- **API Test**: RestAssured
- **Mock Server**: WireMock
- **Raporlama**: Allure
- **Build Tool**: Maven



