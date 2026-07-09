# Sistema de Bodegas Distribuidas

Sistema de gestión de inventario para bodegas, construido con una arquitectura de microservicios sobre **Spring Boot 3.2.5 / Java 21**, con descubrimiento de servicios vía **Eureka**, enrutamiento centralizado vía **Spring Cloud Gateway**, persistencia en **Oracle Cloud Autonomous Database**, y despliegue contenerizado en **Docker** / **Render**.

Proyecto académico — DSY1103, DuocUC.

---

## Arquitectura

```
                         ┌──────────────────┐
                         │   ms-gateway     │  :8080
                         │ (Spring Cloud    │
                         │   Gateway)       │
                         └────────┬─────────┘
                                  │ resuelve rutas vía Eureka
                                  ▼
                         ┌──────────────────┐
                         │  eureka-server   │  :8761
                         │ (Service Registry)│
                         └────────┬─────────┘
                                  │ heartbeat / registro
        ┌─────────────┬──────────┼──────────┬─────────────┐
        ▼             ▼          ▼          ▼             ▼
   ms-categorias  ms-producto  ms-kardex  ms-bodega   ms-proveedores
   ms-usuarios    ms-ingresos  ms-egresos ms-ajuste   ms-traslados
        │             │          │          │             │
        └─────────────┴──────────┴──────────┴─────────────┘
                                  │
                                  ▼
                    Oracle Cloud Autonomous DB
                    (un schema por microservicio)
```

Cada microservicio se registra en Eureka al arrancar y expone su propia base de datos (patrón **Database per Service**). El Gateway es el único punto de entrada público; resuelve cada ruta contra el registro de Eureka en tiempo real, sin IPs hardcodeadas, y aplica Circuit Breaker para degradar con gracia si algún microservicio no responde.

---

## Stack tecnológico

| Capa | Tecnología |
|---|---|
| Lenguaje / Framework | Java 21, Spring Boot 3.2.5 |
| Service Discovery | Netflix Eureka |
| API Gateway | Spring Cloud Gateway + Resilience4j (Circuit Breaker) |
| Persistencia | Spring Data JPA + Oracle Cloud Autonomous DB (Oracle Wallet) |
| Comunicación entre servicios | WebClient (Spring WebFlux, no bloqueante) |
| Documentación de API | Springdoc OpenAPI / Swagger UI |
| Testing | JUnit 5 + Mockito |
| Contenerización | Docker (multi-stage build) |
| Orquestación local | Docker Compose |
| Despliegue | Render (`render.yaml`) |

---

## Microservicios

| Servicio | Puerto | Responsabilidad | Endpoint base |
|---|---|---|---|
| `eureka-server` | 8761 | Registro y descubrimiento de servicios | `/eureka` |
| `ms-gateway` | 8080 | Punto de entrada único, enrutamiento y resiliencia | `/api/**` |
| `ms-categorias` | 8081 | Catálogo de categorías de producto | `/api/categorias` |
| `ms-producto` | 8082 | Catálogo de productos | `/api/productos` |
| `ms-egresos` | 8083 | Salidas de stock | `/api/egresos` |
| `ms-ingresos` | 8084 | Entradas de stock | `/api/ingresos` |
| `ms-bodega` | 8085 | Ubicaciones/bodegas físicas | `/api/ubicaciones` |
| `ms-kardex` | 8086 | Trazabilidad de movimientos y stock actual | `/api/kardex` |
| `ms-traslados` | 8087 | Traslados de stock entre bodegas | `/api/traslados` |
| `ms-ajuste` | 8088 | Ajustes manuales de inventario | `/api/ajustes` |
| `ms-proveedores` | 8089 | Gestión de proveedores | `/api/proveedores` |
| `ms-usuarios` | 8090 | Gestión de usuarios y roles | `/api/usuarios` |

Todo el tráfico externo pasa por el Gateway en el puerto **8080**; los puertos individuales solo se exponen en el entorno local de desarrollo.

---

## Ejecución local con Docker Compose

### Requisitos previos
- Docker y Docker Compose instalados
- Oracle Wallet del proyecto (carpeta `Wallet_*`, no se versiona por seguridad)

### Pasos

```bash
# 1. Ubicar la wallet de Oracle en la ruta que espera docker-compose.yml
#    (ver la sección "volumes" de cada servicio)

# 2. Levantar todo el ecosistema
docker compose up --build

# 3. Verificar que Eureka ve todos los servicios registrados
#    http://localhost:8761

# 4. Probar el Gateway
curl http://localhost:8080/api/kardex/movimientos
```

Docker Compose levanta primero `eureka-server` y espera su healthcheck (`condition: service_healthy`) antes de iniciar el resto de los servicios, evitando que intenten registrarse contra un Eureka que aún no está listo.

---

## Despliegue en Render

El archivo `render.yaml` define cada microservicio como un servicio `web` con `runtime: docker`, apuntando a su propio `Dockerfile` vía `rootDir`. Las variables compartidas (credenciales de Oracle, configuración de Eureka) se centralizan en `envVarGroups` para no repetirlas en cada servicio.

Diferencias clave respecto al entorno local:
- Los microservicios se registran en Eureka por **hostname público**, no por IP (los contenedores de Render no comparten red interna).
- La Oracle Wallet se inyecta como variable de entorno en base64 (`WALLET_BASE64`) y se decodifica al arrancar el contenedor, en vez de montarse como volumen.

---

## Pruebas unitarias

Cada microservicio incluye tests con **JUnit 5** y **Mockito** sobre la capa de servicio, aislando repositorios y clientes `WebClient` mediante `@Mock` / `@InjectMocks`, sin necesidad de levantar base de datos ni otros microservicios.

```bash
# Ejecutar tests de un microservicio puntual
cd ms-kardex
./mvnw test
```

---

## Documentación de API

Cada microservicio expone su propio Swagger UI:

```
http://localhost:<puerto>/swagger-ui.html
```

Por ejemplo, para `ms-kardex`: `http://localhost:8086/swagger-ui.html`

---

## Estructura de cada microservicio

```
ms-<nombre>/
├── src/main/java/.../
│   ├── controller/      # Endpoints REST
│   ├── service/         # Lógica de negocio
│   ├── repository/      # Acceso a datos (Spring Data JPA)
│   ├── model/            # Entidades JPA
│   ├── dto/              # Request/Response DTOs
│   ├── config/           # WebClient, beans de configuración
│   └── exception/         # Manejo centralizado de errores
├── src/main/resources/
│   ├── application.yaml  # Configuración (datasource, Eureka, server.port)
│   └── wallet/             # Credenciales Oracle (no versionado)
├── src/test/java/.../     # Tests JUnit 5 + Mockito
└── Dockerfile              # Build multi-stage (Maven → JRE)
```
Proyecto de prueba elaborado por:
Daniel Azocar
Angelica Araya
