# Design System · Hs Pro

> **Mood**: 清晨图书馆 — 通透晨光，专注静谧
> **Register**: product (管理后台)
> **Strategy**: Committed — 钴蓝主色承载 30-60% 视觉面积
> **Style**: 克制但不冷漠，现代但不高冷，专业而有温度

---

## 1. Color Palette (OKLCH)

| Token | Role | Value | Usage |
|---|---|---|---|
| `--hs-primary` | 主色 | `oklch(0.52 0.165 230)` | 按钮、选中态、强调元素 |
| `--hs-primary-hover` | 主色悬停 | `oklch(0.44 0.175 230)` | 主按钮 hover |
| `--hs-primary-light` | 主色浅底 | `oklch(0.88 0.04 230)` | tag 背景、选中行底色 |
| `--hs-primary-subtle` | 主色微底 | `oklch(0.95 0.015 230)` | 表格 hover 行、微强调区 |
| `--hs-accent` | 强调色（珊瑚） | `oklch(0.58 0.19 20)` | 标签、状态点、链接色 |
| `--hs-accent-light` | 强调色浅底 | `oklch(0.88 0.06 20)` | 强调色 tag 背景 |
| `--hs-bg` | 页面底色 | `oklch(1 0 0)` | 纯白 — 最关键的架构选择 |
| `--hs-surface` | 卡片底 | `oklch(0.985 0.001 230)` | pro-card 背景，极微量主色偏向 |
| `--hs-sidebar` | 侧边栏 | `oklch(0.10 0.02 240)` | 深海军蓝，不纯黑 |
| `--hs-sidebar-hover` | 侧边栏悬停 | `oklch(0.17 0.035 240)` | 菜单 hover |
| `--hs-sidebar-active` | 侧边栏选中 | `oklch(0.22 0.04 240)` | 菜单 active |
| `--hs-header` | 顶栏 | `oklch(1 0 0)` | 纯白 |
| `--hs-ink` | 正文 | `oklch(0.20 0.015 250)` | 主文字，≥7:1 vs bg |
| `--hs-ink-secondary` | 次级文字 | `oklch(0.48 0.01 250)` | 描述、辅助信息 |
| `--hs-ink-muted` | 弱文字 | `oklch(0.65 0.008 250)` | placeholder、提示 |
| `--hs-ink-inverse` | 反色文字 | `oklch(0.97 0.002 230)` | 深色背景上文字 |
| `--hs-border` | 边框 | `oklch(0.91 0.004 220)` | 卡片、表格边框 |
| `--hs-border-light` | 浅边框 | `oklch(0.95 0.002 220)` | 分割线 |
| `--hs-success` | 成功 | `oklch(0.62 0.18 145)` | 成功状态 |
| `--hs-warning` | 警告 | `oklch(0.68 0.17 85)` | 警告状态 |
| `--hs-danger` | 危险 | `oklch(0.52 0.19 25)` | 错误、删除 |
| `--hs-shadow-sm` | 阴影浅 | `0 1px 3px oklch(0.20 0.01 250 / 0.06)` | 卡片默认 |
| `--hs-shadow-md` | 阴影中 | `0 4px 12px oklch(0.20 0.01 250 / 0.07)` | 卡片 hover |
| `--hs-shadow-lg` | 阴影深 | `0 8px 24px oklch(0.20 0.01 250 / 0.08)` | 弹窗、下拉 |

## 2. Typography

```
Font stack: "Inter", "SF Pro Text", "PingFang SC", "Microsoft YaHei", system-ui, sans-serif
Base size: 14px / line-height 1.5
```

| Level | Size/Weight | Usage |
|---|---|---|
| 页面标题 (h2) | 17px / 700 | topbar、card-header |
| 卡片标题 | 16px / 600 | `.pro-card-title` |
| 大数字 | 30px / 700 / -0.02em | `.stat-value` |
| 正文 | 14px / 400 | 默认 |
| 标签文字 | 13px / 600 | `.field-label` |
| 辅助文字 | 12–13px / 400 | breadcrumb, hint |

- **不做 fluid typography**（不用 clamp），固定 rem
- 所有标题 `letter-spacing: -0.01em ~ -0.02em`，不超 -0.04em 底线
- 数字用 `font-variant-numeric: tabular-nums` 保持对齐

## 3. Layout Structure

```
┌─────────────────────────────────────────────┐
│  Login Page                                 │
│  ┌──────────┬───────────────┐               │
│  │ 48%      │ 52%           │               │
│  │ 钴蓝底    │ 纯白底         │               │
│  │ 品牌区    │ 登录表单       │               │
│  │ +几何图形 │               │               │
│  └──────────┴───────────────┘               │
├─────────────────────────────────────────────┤
│  Layout (登录后)                             │
│  ┌─────────┬──────────────────────────┐     │
│  │ Sidebar │ Topbar (56px)            │     │
│  │ 236px   │ breadcrumb + search      │     │
│  │ Navy    ├──────────────────────────┤     │
│  │         │ Content                  │     │
│  │ Logo    │ padding: 24px            │     │
│  │ Nav     │                          │     │
│  │ User    │                          │     │
│  └─────────┴──────────────────────────┘     │
└─────────────────────────────────────────────┘
```

**Grid 间距体系**：
- 页面级 gap: 16–20px
- 卡片内 padding: 16–20px
- 工具栏 gap: 10px
- 表单字段 gap: 20px

**圆角**：
- `sm` (6px): 输入框、tag、小按钮
- `md` (10px): 卡片、主按钮
- `lg` (14px): 弹窗

## 4. Component Specifications

### 4.1 统计卡片 (Stat Card)

```
┌──────────────────────┐
│ [icon 44×44]  Label  │   icon 背景色按语义（蓝/绿/橙/紫）
│               30     │   大数字 30px / 700
│               +5.8%  │   提示文字 12px muted
└──────────────────────┘
```
- hover: `box-shadow: var(--hs-shadow-md)` + `border-color: var(--hs-primary)`
- transition: 0.2s ease

### 4.2 数据卡片 (Pro Card)

```css
.pro-card {
  background: var(--hs-surface);
  border: 1px solid var(--hs-border);
  border-radius: var(--hs-radius-md); /* 10px */
  box-shadow: var(--hs-shadow-sm);
}
.pro-card-header { /* 16px padding, 底部 1px border */ }
.pro-card-body   { /* 20px padding */ }
.pro-card-title  { /* 16px / 600 */ }
```

### 4.3 侧边栏导航

- 宽度: 236px
- 背景: `--hs-sidebar` (深海军蓝)
- 菜单项: `border-radius: 9px`, `margin: 2px 8px`
- 默认文字: `oklch(1 0 0 / 0.6)`
- hover: `--hs-sidebar-hover`
- active: `--hs-sidebar-active` + 右侧小圆点指示器
- 底部用户区: 头像 + 名称 + 角色 + 退出按钮

### 4.4 顶栏 (Topbar)

- 高度: 56px
- 背景: 纯白
- 左侧: 页面标题 (17px/700) + 面包屑 (12px muted)
- 右侧: 搜索自动完成 (220px)
- 底部分割线: `1px solid var(--hs-border-light)`

### 4.5 主按钮 (Primary Button)

```css
height: 44px;
border-radius: var(--hs-radius-md); /* 10px */
background: var(--hs-primary);
color: white;
font-weight: 600;
```
- hover: 背景变深 + `translateY(-1px)`
- active: 回弹 `translateY(0)`
- disabled: `opacity: 0.6`
- transition: 0.2s ease

### 4.6 Element Plus 覆盖

通过 CSS 变量统一覆盖 Element Plus 默认样式（已在 `style.css` 中完成）:
- `--el-color-primary` → `--hs-primary`
- `--el-color-success/warning/danger` → 对应语义色
- `--el-bg-color` → `--hs-bg`
- `--el-text-color-*` → `--hs-ink-*`
- `--el-border-color*` → `--hs-border*`
- 表格 header 背景微调为 `oklch(0.975 0.001 230)`
- 表格行 hover 用 `--hs-primary-subtle`
- 菜单、tag、分页器等都有对应覆盖

## 5. Page-by-Page Design

### 5.1 LoginView (/login)
- **布局**: 48/52 左右分栏，移动端单列
- **左侧**: 钴蓝 (`--hs-primary`) 全高面板
  - 多层径向渐变底层
  - 细小点阵纹路 28px
  - 5 个抽象几何图形 (圆、方、菱形、环) 带浮动动画
  - 珊瑚色点缀图形
  - 品牌名 "Hs Pro" 48px/800 白色
  - 副标题 + 描述文字
  - 底部隐私链接
- **右侧**: 纯白底，居中表单
  - 标题 "欢迎回来" 28px/700
  - 账号/密码输入框 (large size, 带图标)
  - 记住账号 / 忘记密码
  - 主按钮 44px 全宽
  - 加载态带三点弹跳动画
- **动画**: 几何图形 7–11s 缓动 float
- **响应式**: ≤800px 单列，左侧压缩到 180px

### 5.2 LayoutView (登录后外壳)
- **侧边栏**: 236px 深海军蓝
  - 品牌区: SVG logo + "Hs Pro" 19px/700
  - 导航: 6 个菜单项（按角色过滤），带图标和 active 圆点
  - 底部: 头像 + 用户名/角色 + 退出按钮
- **顶栏**: 56px
  - 左侧: 页面标题 + 面包屑
  - 右侧: 搜索自动完成
- **内容区**: flex:1, overflow-y:auto, padding 24px

### 5.3 DashboardView (/dashboard)
- **统计卡片**: 4 列 grid (响应式 2→1 列)
  - 带语义色 icon 背景 (蓝/绿/橙/紫)
  - hover 效果
- **图表区**: 2×2 grid (响应式单列)
  - 员工职位分布 → 渐变柱状图 (钴蓝)
  - 员工性别分布 → 环形饼图 (蓝+橙)
  - 班级人数趋势 → 渐变柱状图 (绿)
  - 学员学历结构 → 饼图 (6 色)
- 图表使用 ECharts，带圆角 bar 和分离间隙

### 5.4 EmpView / DeptView / ClazzView / StudentView / LogView
- 统一使用 `pro-card` 包裹
- 工具栏: `pro-toolbar` 弹性布局
- 表格: Element Plus `el-table` border stripe，样式由全局 CSS 覆盖
- 分页: 右对齐
- 弹窗: Element Plus `el-dialog`，圆角 14px

## 6. Motion Rules

- 过渡时长: 150–250ms，`ease` 曲线
- 不动画 CSS layout 属性
- hover 效果: 轻微 `translateY(-1px)` + shadow 变化
- 所有动画包裹 `@media (prefers-reduced-motion: reduce)`
- 无页面级入场编排动画

## 7. What's Been Done vs To Do

### ✅ 已完成

| 文件 | 状态 |
|---|---|
| `PRODUCT.md` | 已写 |
| `src/style.css` | 完整设计系统 + Element Plus 覆盖 + 9 个通用工具类 |
| `src/views/LoginView.vue` | 全新 redesign |
| `src/views/LayoutView.vue` | 全新 redesign |
| `src/views/DashboardView.vue` | 全新 redesign |
| `src/views/EmpView.vue` | 内联 style → class 优化完成 |
| `src/views/DeptView.vue` | 已足够简洁，无需改动 |
| `src/views/ClazzView.vue` | 内联 style → class 优化完成 |
| `src/views/StudentView.vue` | 内联 style → class 优化完成 |
| `src/views/LogView.vue` | 内联 style → class 优化完成 |
| `src/main.js` | 移除旧的 global.css / pro-theme.css 引用 |
| `index.html` | 更新 title 和 lang |
| `src/styles/global.css` | 已删除（合并到 style.css） |
| `src/styles/pro-theme.css` | 已删除（合并到 style.css） |
| `src/components/HelloWorld.vue` | 已删除（无用） |

### 🔲 待完成

| 文件 | 需要做的 |
|---|---|
| `public/favicon.svg` | 可选：替换为 Hs Pro 品牌 favicon |
| `.impeccable/live/config.json` | 可选：配置 live mode |

## 8. Key Design Decisions (给后续模型的指引)

1. **纯白底色是故意的** — 不是偷懒。Stripe/Linear/Notion 都在纯白上跑，温暖感由主色和排版承载，不在底色里加暖色调。
2. **珊瑚色 accent 和钴蓝 primary 是互补但有距离的** — 不是红蓝正对撞，珊瑚偏暖橙，钴蓝偏冷，形成有张力的对比但不刺眼。
3. **深海军蓝侧边栏不是纯黑** — `oklch(0.10 0.02 240)` 带极微量色相，比 `#000` 更柔和，在暗环境下不刺眼。
4. **固定字号，不做 fluid typography** — 管理后台用户在同 DPI 下工作，clamp 缩放的标题在侧边栏里不会更好看。
5. **所有 Element Plus 覆盖走 CSS 变量** — 不逐组件覆盖样式，保证一致性和可维护性。
6. **不要乱加装饰** — 没有 gradient text、没有 glassmorphism 默认、没有 sketchy SVG、没有 side-stripe border、没有 bouncing 动画。干净即高级。

---

**Build 验证**: `npx vite build` 已通过，2235 modules 编译成功。
