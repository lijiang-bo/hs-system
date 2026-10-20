# Hs Pro —— 培训机构后台管理系统

基于 **Spring Boot 3 + Vue 3** 的前后端分离全栈管理系统，涵盖员工、部门、班级、学员管理与操作日志审计功能。

---

## 技术栈

### 后端

| 技术                    | 版本     | 用途                  |
| ----------------------- | -------- | --------------------- |
| Spring Boot             | 3.3.7    | 基础框架              |
| Java                    | 17       | 运行环境              |
| MyBatis                 | 3.0.4    | ORM / 数据访问        |
| MySQL Connector         | 8.x      | 数据库驱动            |
| PageHelper              | 1.4.7    | 物理分页              |
| JJWT                    | 0.12.6   | JWT 令牌签发与校验    |
| FastJSON2               | 2.0.43   | JSON 序列化           |
| Apache HttpClient       | 4.5.14   | HTTP 客户端           |
| Spring AOP              | 内置     | 权限校验 & 操作日志   |
| Lombok                  | 内置     | 代码简化              |

### 前端

| 技术                    | 版本     | 用途                  |
| ----------------------- | -------- | --------------------- |
| Vue                     | 3.5.32   | 渐进式框架            |
| Vite                    | 8.0.4    | 构建工具 & 开发服务器 |
| Element Plus            | 2.13.7   | UI 组件库             |
| Vue Router              | 5.0.4    | 前端路由              |
| Pinia                   | 3.0.4    | 状态管理              |
| Axios                   | 1.15.0   | HTTP 请求             |
| ECharts                 | 6.0.0    | 数据可视化            |

---

## 项目结构

```
Hs-system/
├── Hs-web-backended/                # 后端 Spring Boot 项目
│   ├── src/main/java/com/jsu/
│   │   ├── HsWebManagementApplication.java   # 启动类
│   │   ├── anno/                    # 自定义注解 (@LogOperation, @RequireRole)
│   │   ├── aspect/                  # AOP 切面 (操作日志, 角色权限)
│   │   ├── config/                  # 配置类 (静态资源映射)
│   │   ├── controller/              # 控制器 (8个)
│   │   ├── exception/               # 全局异常处理
│   │   ├── filter/                  # JWT 过滤器
│   │   ├── mapper/                  # MyBatis Mapper 接口
│   │   ├── pojo/                    # 实体类 & DTO
│   │   ├── service/                 # 服务接口
│   │   │   └── Impl/                # 服务实现
│   │   └── util/                    # 工具类 (JwtUtils, CurrentHolder)
│   ├── src/main/resources/
│   │   ├── application.yml          # 应用配置
│   │   └── com.jsu.mapper/          # Mapper XML (5个)
│   └── pom.xml                      # Maven 依赖
│
├── Hs-vue-fronted/                  # 前端 Vue 3 项目
│   ├── src/
│   │   ├── api/                     # API 接口封装 (http.js + index.js)
│   │   ├── router/                  # Vue Router 路由配置
│   │   ├── stores/                  # Pinia 状态管理
│   │   ├── views/                   # 页面组件 (7个)
│   │   └── components/             # 公共组件
│   ├── vite.config.js               # Vite 配置 (代理)
│   ├── package.json                 # 依赖声明
│   └── index.html                  # 入口 HTML
│
└── README.md                        # 项目说明文档
```

---

## 功能模块

### 1. 登录认证
- 用户名 + 密码登录
- JWT Token 签发，12 小时有效期
- Token 存储于 localStorage，请求时自动携带

### 2. 总览看板 (Dashboard)
- ECharts 可视化图表
- 员工职位分布、性别占比、班级人数、学员学历统计

### 3. 员工管理
- 分页查询、新增、编辑、删除
- 员工头像上传
- 员工工作经历管理
- 角色过滤（超级管理员 > 管理员 > 普通员工）

### 4. 部门管理
- 部门列表查询、新增、编辑、删除
- 有员工关联的部门禁止删除

### 5. 班级管理
- 班级分页查询、新增、编辑、删除（含学科、教室、班主任等信息）
- 有学员关联的班级禁止删除

### 6. 学员管理
- 学员分页查询、新增、编辑、删除
- 学员违纪登记（扣分机制）

### 7. 操作日志
- 自动记录所有标注 `@LogOperation` 的操作
- 记录操作人、耗时、请求参数（密码自动脱敏）

---

## 数据库设计

**数据库名**: `hs`

| 表名          | 说明             |
| ------------- | ---------------- |
| `emp`         | 员工表           |
| `dept`        | 部门表           |
| `emp_expr`    | 员工工作经历表   |
| `clazz`       | 班级表           |
| `student`     | 学员表           |
| `operate_log` | 操作日志审计表   |

**角色枚举**:

| role | 角色名称     | 权限范围                       |
| ---- | ------------ | ------------------------------ |
| 1    | 超级管理员   | 全部功能，管理所有角色         |
| 2    | 管理员       | 管理普通员工，查看下级数据     |
| 3    | 普通员工     | 仅查看权限，不可新增/编辑/删除 |

---

## 快速启动

### 环境要求

- **JDK**: 17+
- **Node.js**: 18+
- **MySQL**: 8.x
- **Maven**: 3.6+

### 1. 初始化数据库

创建数据库 `hs`，导入初始 SQL（见 `db/init.sql`）：

```sql
CREATE DATABASE IF NOT EXISTS hs DEFAULT CHARACTER SET utf8mb4;
```

### 2. 配置上传目录

在 D 盘创建图片上传目录：

```bash
mkdir D:\images
```

> 如需修改路径，编辑 `Hs-web-backended/src/main/resources/application.yml` 中的 `file.upload-dir`。

### 3. 修改数据库连接

编辑 `Hs-web-backended/src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hs
    username: root
    password: 你的密码
```

### 4. 启动后端

```bash
cd Hs-web-backended
mvn spring-boot:run
```

> 后端启动在 `http://localhost:8080`

### 5. 启动前端

```bash
cd Hs-vue-fronted
npm install
npm run dev
```

> 前端启动在 `http://localhost:5173`，API 请求自动代理到 `localhost:8080`

### 6. 访问系统

浏览器打开 `http://localhost:5173`，使用预置账号登录：

| 账号         | 密码   | 角色       |
| ------------ | ------ | ---------- |
| **admin**    | 123456 | 超级管理员 |
| zhangsan     | 123456 | 管理员     |
| lisi         | 123456 | 普通员工   |

---

## 权限矩阵

### 前端路由权限

| 路由          | 页面         | 超级管理员(1) | 管理员(2) | 普通员工(3) |
| ------------- | ------------ | :-----------: | :-------: | :---------: |
| `/dashboard`  | 总览看板     | ✅            | ✅        | ✅          |
| `/emps`       | 员工管理     | ✅            | ✅        | ❌          |
| `/depts`      | 部门管理     | ✅            | ✅        | ✅          |
| `/clazzs`     | 班级管理     | ✅            | ✅        | ✅          |
| `/students`   | 学员管理     | ✅            | ✅        | ✅          |
| `/logs`       | 操作日志     | ✅            | ✅        | ❌          |

### 双重鉴权机制

1. **前端路由守卫** (`router.beforeEach`)：根据 `role` 判断能否进入页面，无权限自动重定向
2. **后端 AOP 切面** (`@RequireRole`)：对 Controller 方法做二次校验，防止越权操作

---

## API 接口一览

### 登录模块

| 方法 | 路径           | 说明     |
| ---- | -------------- | -------- |
| POST | `/login`       | 用户登录 |

### 员工模块

| 方法   | 路径              | 说明         |
| ------ | ----------------- | ------------ |
| GET    | `/emps`           | 分页查询     |
| GET    | `/emps/list`      | 全部员工列表 |
| GET    | `/emps/{id}`      | 员工详情     |
| POST   | `/emps`           | 新增员工     |
| PUT    | `/emps`           | 修改员工     |
| DELETE | `/emps?ids=1,2,3` | 批量删除     |

### 部门模块

| 方法   | 路径             | 说明       |
| ------ | ---------------- | ---------- |
| GET    | `/depts`         | 部门列表   |
| POST   | `/depts`         | 新增部门   |
| PUT    | `/depts`         | 修改部门   |
| DELETE | `/depts/{id}`    | 删除部门   |

### 班级模块

| 方法   | 路径              | 说明       |
| ------ | ----------------- | ---------- |
| GET    | `/clazzs`         | 分页查询   |
| GET    | `/clazzs/list`    | 全部班级   |
| GET    | `/clazzs/{id}`    | 班级详情   |
| POST   | `/clazzs`         | 新增班级   |
| PUT    | `/clazzs`         | 修改班级   |
| DELETE | `/clazzs/{id}`    | 删除班级   |

### 学员模块

| 方法   | 路径                                  | 说明             |
| ------ | ------------------------------------- | ---------------- |
| GET    | `/students`                           | 分页查询         |
| GET    | `/students/{id}`                      | 学员详情         |
| POST   | `/students`                           | 新增学员         |
| PUT    | `/students`                           | 修改学员         |
| DELETE | `/students/{id}`                      | 删除学员         |
| PUT    | `/students/violation/{id}/{score}`    | 违纪扣分         |

### 上传模块

| 方法 | 路径      | 说明         |
| ---- | --------- | ------------ |
| POST | `/upload` | 文件上传     |

### 报表模块

| 方法 | 路径                       | 说明           |
| ---- | -------------------------- | -------------- |
| GET  | `/report/empJobData`       | 员工职位分布   |
| GET  | `/report/empGenderData`    | 员工性别统计   |
| GET  | `/report/studentCountData` | 班级人数统计   |
| GET  | `/report/studentDegreeData`| 学员学历分布   |

### 日志模块

| 方法 | 路径         | 说明             |
| ---- | ------------ | ---------------- |
| GET  | `/log/page`  | 操作日志分页查询 |

---

## 架构设计要点

| 设计                  | 实现方式                                    |
| --------------------- | ------------------------------------------- |
| 分层架构              | Controller → Service → Mapper 三层清晰解耦  |
| JWT 认证              | `TokenFilter` 过滤 + 白名单放行             |
| ThreadLocal 线程隔离   | `CurrentHolder` 存储当前用户上下文          |
| AOP 角色校验          | `@RequireRole` 注解 + `RoleCheckAspect` 切面 |
| AOP 操作日志          | `@LogOperation` 注解 + `OperationLogAspect` 切面（含密码脱敏） |
| 全局异常处理          | `GlobalExceptionHandler` 捕获 7 种异常类型   |
| 统一响应格式          | `Result(code, msg, data)` + `PageResult`     |
| 前端路由守卫          | `router.beforeEach` 实现页面级权限控制       |
| 请求/响应拦截         | Axios 拦截器自动注入 Token + 统一错误提示    |
