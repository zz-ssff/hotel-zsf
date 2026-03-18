<template>
  <div class="app" :class="{ 'dark-theme': isDarkMode }">
    <div class="theme-toggle" @click="toggleTheme">
      <el-icon v-if="isDarkMode"><Sunny /></el-icon>
      <el-icon v-else><Moon /></el-icon>
    </div>
    <router-view />
  </div>
</template>

<script>
import { ref, watch, onMounted } from 'vue'
import { Sunny, Moon } from '@element-plus/icons-vue'

export default {
  name: 'App',
  components: {
    Sunny,
    Moon
  },
  setup() {
    const isDarkMode = ref(false)

    // 从localStorage加载主题设置
    onMounted(() => {
      const savedTheme = localStorage.getItem('theme')
      if (savedTheme) {
        isDarkMode.value = savedTheme === 'dark'
        document.documentElement.setAttribute('data-theme', savedTheme)
      }
    })

    // 监听主题变化并保存到localStorage
    watch(isDarkMode, (newValue) => {
      const theme = newValue ? 'dark' : 'light'
      localStorage.setItem('theme', theme)
      document.documentElement.setAttribute('data-theme', theme)
    })

    // 切换主题
    const toggleTheme = () => {
      isDarkMode.value = !isDarkMode.value
    }

    return {
      isDarkMode,
      toggleTheme
    }
  }
}
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

:root {
  /* 白天主题变量 - 高端现代风格 */
  --primary-color: #3b82f6;
  --secondary-color: #8b5cf6;
  --success-color: #10b981;
  --warning-color: #f59e0b;
  --danger-color: #ef4444;
  --info-color: #6b7280;
  
  --bg-color: #f8fafc;
  --card-bg: #ffffff;
  --text-color: #1e293b;
  --text-color-secondary: #64748b;
  --border-color: #e2e8f0;
  --hover-color: #ebf8ff;
  
  --header-bg: #1e293b;
  --header-text: #ffffff;
  --aside-bg: #ffffff;
  --aside-text: #475569;
  --aside-active-bg: #f1f5f9;
  --aside-active-text: #3b82f6;
  
  --shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  --shadow-hover: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  --shadow-card: 0 1px 3px 0 rgba(0, 0, 0, 0.1), 0 1px 2px 0 rgba(0, 0, 0, 0.06);
  --shadow-card-hover: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  
  --transition: all 0.3s ease;
  --border-radius: 0.5rem;
  --border-radius-lg: 0.75rem;
  --border-radius-xl: 1rem;
  
  --spacing-xs: 0.25rem;
  --spacing-sm: 0.5rem;
  --spacing-md: 1rem;
  --spacing-lg: 1.5rem;
  --spacing-xl: 2rem;
  --spacing-2xl: 3rem;
}

[data-theme="dark"] {
  /* 夜间主题变量 - 高端现代暗色调 */
  --primary-color: #60a5fa;
  --secondary-color: #a78bfa;
  --success-color: #34d399;
  --warning-color: #fbbf24;
  --danger-color: #f87171;
  --info-color: #94a3b8;
  
  --bg-color: #0f172a;
  --card-bg: #1e293b;
  --text-color: #f8fafc;
  --text-color-secondary: #cbd5e1;
  --border-color: #334155;
  --hover-color: #334155;
  
  --header-bg: #1e293b;
  --header-text: #f8fafc;
  --aside-bg: #1e293b;
  --aside-text: #cbd5e1;
  --aside-active-bg: #334155;
  --aside-active-text: #60a5fa;
  
  --shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.3), 0 2px 4px -1px rgba(0, 0, 0, 0.2);
  --shadow-hover: 0 10px 15px -3px rgba(0, 0, 0, 0.4), 0 4px 6px -2px rgba(0, 0, 0, 0.3);
  --shadow-card: 0 1px 3px 0 rgba(0, 0, 0, 0.3), 0 1px 2px 0 rgba(0, 0, 0, 0.2);
  --shadow-card-hover: 0 10px 15px -3px rgba(0, 0, 0, 0.4), 0 4px 6px -2px rgba(0, 0, 0, 0.3);
}

.app {
  min-height: 100vh;
  font-family: 'Inter', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: var(--bg-color);
  color: var(--text-color);
  transition: var(--transition);
  background-image: radial-gradient(circle at 10% 20%, rgba(59, 130, 246, 0.05) 0%, rgba(255, 255, 255, 0) 90%);
}

.theme-toggle {
  position: fixed;
  top: var(--spacing-lg);
  right: var(--spacing-lg);
  width: 56px;
  height: 56px;
  border-radius: 50%;
  background-color: var(--card-bg);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  box-shadow: var(--shadow-card);
  transition: var(--transition);
  z-index: 1000;
  border: 1px solid var(--border-color);
}

.theme-toggle:hover {
  box-shadow: var(--shadow-card-hover);
  transform: scale(1.1);
  border-color: var(--primary-color);
}

.theme-toggle el-icon {
  font-size: 24px;
  color: var(--text-color);
  transition: var(--transition);
}

.theme-toggle:hover el-icon {
  color: var(--primary-color);
}

/* 全局卡片样式 */
.el-card {
  background-color: var(--card-bg) !important;
  border: 1px solid var(--border-color) !important;
  border-radius: var(--border-radius-lg) !important;
  box-shadow: var(--shadow-card) !important;
  transition: var(--transition) !important;
  overflow: hidden !important;
}

.el-card:hover {
  box-shadow: var(--shadow-card-hover) !important;
  transform: translateY(-2px) !important;
}

.el-card__header {
  background-color: var(--card-bg) !important;
  border-bottom: 1px solid var(--border-color) !important;
  padding: var(--spacing-lg) !important;
  font-weight: 600 !important;
  font-size: 1.1rem !important;
}

.el-card__body {
  padding: var(--spacing-lg) !important;
}

/* 全局按钮样式 */
.el-button {
  transition: var(--transition) !important;
  border-radius: var(--border-radius) !important;
  font-weight: 500 !important;
  padding: 0.5rem 1rem !important;
  border: 1px solid transparent !important;
}

.el-button:hover {
  transform: translateY(-1px) !important;
  box-shadow: var(--shadow-card-hover) !important;
}

.el-button--primary {
  background-color: var(--primary-color) !important;
  border-color: var(--primary-color) !important;
  color: white !important;
}

.el-button--primary:hover {
  background-color: #2563eb !important;
  border-color: #2563eb !important;
}

.el-button--success {
  background-color: var(--success-color) !important;
  border-color: var(--success-color) !important;
  color: white !important;
}

.el-button--success:hover {
  background-color: #059669 !important;
  border-color: #059669 !important;
}

.el-button--warning {
  background-color: var(--warning-color) !important;
  border-color: var(--warning-color) !important;
  color: white !important;
}

.el-button--warning:hover {
  background-color: #d97706 !important;
  border-color: #d97706 !important;
}

.el-button--danger {
  background-color: var(--danger-color) !important;
  border-color: var(--danger-color) !important;
  color: white !important;
}

.el-button--danger:hover {
  background-color: #dc2626 !important;
  border-color: #dc2626 !important;
}

.el-button--info {
  background-color: var(--info-color) !important;
  border-color: var(--info-color) !important;
  color: white !important;
}

.el-button--info:hover {
  background-color: #4b5563 !important;
  border-color: #4b5563 !important;
}

.el-button--default {
  background-color: var(--card-bg) !important;
  border-color: var(--border-color) !important;
  color: var(--text-color) !important;
}

.el-button--default:hover {
  background-color: var(--hover-color) !important;
  border-color: var(--primary-color) !important;
  color: var(--primary-color) !important;
}

/* 全局表格样式 */
.el-table {
  background-color: var(--card-bg) !important;
  border: 1px solid var(--border-color) !important;
  border-radius: var(--border-radius-lg) !important;
  overflow: hidden !important;
  box-shadow: var(--shadow-card) !important;
}

.el-table th {
  background-color: var(--hover-color) !important;
  color: var(--text-color) !important;
  border-bottom: 1px solid var(--border-color) !important;
  padding: var(--spacing-md) !important;
  font-weight: 600 !important;
  font-size: 0.9rem !important;
}

.el-table td {
  border-bottom: 1px solid var(--border-color) !important;
  color: var(--text-color) !important;
  padding: var(--spacing-md) !important;
}

.el-table__row {
  transition: var(--transition) !important;
}

.el-table__row:hover {
  background-color: var(--hover-color) !important;
  transform: scale(1.01) !important;
}

/* 表格单元格样式 */
.el-table__cell {
  background-color: var(--card-bg) !important;
  color: var(--text-color) !important;
  border-right: 1px solid var(--border-color) !important;
}

/* 表格头部样式 */
.el-table__header-wrapper {
  background-color: var(--card-bg) !important;
  border-bottom: 1px solid var(--border-color) !important;
}

/* 表格主体样式 */
.el-table__body-wrapper {
  background-color: var(--card-bg) !important;
}

/* 表格空状态样式 */
.el-table__empty-block {
  background-color: var(--card-bg) !important;
  padding: var(--spacing-2xl) !important;
}

/* 表格空状态文本样式 */
.el-table__empty-text {
  color: var(--text-color-secondary) !important;
  font-size: 1rem !important;
}

/* 全局表单样式 */
.el-form-item {
  margin-bottom: var(--spacing-lg) !important;
}

.el-form-item__label {
  color: var(--text-color) !important;
  font-weight: 500 !important;
  font-size: 0.9rem !important;
}

/* 输入框样式 */
.el-input {
  width: 100% !important;
}

.el-input__wrapper {
  background-color: var(--card-bg) !important;
  border: 1px solid var(--border-color) !important;
  border-radius: var(--border-radius) !important;
  transition: var(--transition) !important;
  box-shadow: none !important;
}

.el-input__wrapper:hover {
  border-color: var(--primary-color) !important;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1) !important;
}

.el-input__wrapper.is-focus {
  border-color: var(--primary-color) !important;
  box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.2) !important;
}

.el-input__inner {
  color: var(--text-color) !important;
  font-size: 0.9rem !important;
  padding: var(--spacing-md) !important;
}

/* 选择器样式 */
.el-select {
  width: 100% !important;
}

.el-select .el-input__wrapper {
  background-color: var(--card-bg) !important;
}

/* 下拉菜单样式 */
.el-select-dropdown {
  background-color: var(--card-bg) !important;
  border: 1px solid var(--border-color) !important;
  border-radius: var(--border-radius) !important;
  box-shadow: var(--shadow-card-hover) !important;
  margin-top: 4px !important;
  min-width: 100% !important;
  width: auto !important;
  max-width: none !important;
  overflow: visible !important;
}

.el-select-dropdown__item {
  color: var(--text-color) !important;
  padding: var(--spacing-md) !important;
  transition: var(--transition) !important;
  white-space: nowrap !important;
  overflow: visible !important;
  text-overflow: clip !important;
  min-width: 100% !important;
  width: auto !important;
}

.el-select-dropdown__item:hover {
  background-color: var(--hover-color) !important;
  transform: translateX(4px) !important;
  margin: 0 !important;
  padding: var(--spacing-md) !important;
}

.el-select-dropdown__item.selected {
  color: var(--primary-color) !important;
  background-color: var(--hover-color) !important;
  font-weight: 500 !important;
  margin: 0 !important;
  padding: var(--spacing-md) !important;
}

/* 修复下拉框选项留白问题 */
.el-select-dropdown__list {
  padding: 0 !important;
  margin: 0 !important;
}

.el-select-dropdown__item {
  margin: 0 !important;
  padding: var(--spacing-md) !important;
  line-height: normal !important;
  height: auto !important;
  min-height: 36px !important;
  display: flex !important;
  align-items: center !important;
}

/* 标签样式 */
.el-tag {
  background-color: var(--card-bg) !important;
  color: var(--text-color) !important;
  border: 1px solid var(--border-color) !important;
  border-radius: var(--border-radius) !important;
  padding: 0.25rem 0.75rem !important;
  font-size: 0.875rem !important;
  font-weight: 500 !important;
}

.el-tag--primary {
  background-color: var(--primary-color) !important;
  border-color: var(--primary-color) !important;
  color: white !important;
}

.el-tag--success {
  background-color: var(--success-color) !important;
  border-color: var(--success-color) !important;
  color: white !important;
}

.el-tag--warning {
  background-color: var(--warning-color) !important;
  border-color: var(--warning-color) !important;
  color: white !important;
}

.el-tag--danger {
  background-color: var(--danger-color) !important;
  border-color: var(--danger-color) !important;
  color: white !important;
}

/* 日期选择器样式 */
.el-date-picker {
  background-color: var(--card-bg) !important;
  border: 1px solid var(--border-color) !important;
  border-radius: var(--border-radius-lg) !important;
  box-shadow: var(--shadow-card-hover) !important;
  overflow: hidden !important;
}

.el-date-picker__header {
  background-color: var(--card-bg) !important;
  border-bottom: 1px solid var(--border-color) !important;
  padding: var(--spacing-md) !important;
}

.el-date-picker__header-label {
  color: var(--text-color) !important;
  font-weight: 600 !important;
  font-size: 1rem !important;
}

.el-date-picker__header-btn {
  color: var(--text-color) !important;
  transition: var(--transition) !important;
}

.el-date-picker__header-btn:hover {
  color: var(--primary-color) !important;
}

.el-date-table {
  background-color: var(--card-bg) !important;
  padding: var(--spacing-md) !important;
}

.el-date-table th {
  color: var(--text-color-secondary) !important;
  font-weight: 500 !important;
  padding: var(--spacing-sm) !important;
}

.el-date-table td {
  color: var(--text-color) !important;
  padding: var(--spacing-sm) !important;
  border-radius: var(--border-radius) !important;
  transition: var(--transition) !important;
}

.el-date-table td:hover {
  background-color: var(--hover-color) !important;
  transform: scale(1.1) !important;
}

.el-date-table td.current:not(.disabled):not(.range-start):not(.range-end):not(.range-middle) {
  background-color: var(--primary-color) !important;
  color: white !important;
  font-weight: 600 !important;
}

.el-date-table td.range-start,
.el-date-table td.range-end {
  background-color: var(--primary-color) !important;
  color: white !important;
  font-weight: 600 !important;
}

.el-date-table td.range-middle {
  background-color: rgba(59, 130, 246, 0.1) !important;
  color: var(--text-color) !important;
}

/* 下拉菜单样式增强 */
.el-popper {
  background-color: var(--card-bg) !important;
  border: 1px solid var(--border-color) !important;
  box-shadow: var(--shadow) !important;
}

/* 输入框样式增强 */
.el-input {
  background-color: var(--card-bg) !important;
}

.el-input__inner {
  background-color: var(--card-bg) !important;
  color: var(--text-color) !important;
}

/* 全局菜单样式 */
.el-menu {
  background-color: var(--aside-bg) !important;
  border-right: 1px solid var(--border-color) !important;
  border-radius: 0 var(--border-radius-lg) var(--border-radius-lg) 0 !important;
  overflow: hidden !important;
  margin: var(--spacing-lg) 0 !important;
  box-shadow: var(--shadow-card) !important;
}

.el-menu-item {
  color: var(--aside-text) !important;
  transition: var(--transition) !important;
  padding: var(--spacing-md) var(--spacing-lg) !important;
  margin: var(--spacing-xs) var(--spacing-sm) !important;
  border-radius: var(--border-radius) !important;
  font-weight: 500 !important;
}

.el-menu-item:hover {
  background-color: var(--hover-color) !important;
  color: var(--primary-color) !important;
  transform: translateX(4px) !important;
}

.el-menu-item.is-active {
  background-color: var(--primary-color) !important;
  color: white !important;
  font-weight: 600 !important;
}

.el-menu-item.is-active:hover {
  background-color: #2563eb !important;
  color: white !important;
}

.el-menu-item .el-icon {
  margin-right: var(--spacing-md) !important;
  font-size: 1.1rem !important;
}

/* 全局分页样式 */
.el-pagination {
  color: var(--text-color) !important;
  margin-top: var(--spacing-xl) !important;
  padding: var(--spacing-md) !important;
  background-color: var(--card-bg) !important;
  border-radius: var(--border-radius-lg) !important;
  box-shadow: var(--shadow-card) !important;
  display: flex !important;
  align-items: center !important;
  justify-content: center !important;
}

.el-pagination__sizes {
  margin-right: var(--spacing-lg) !important;
}

.el-pagination__sizes .el-select {
  min-width: 220px !important;
  width: auto !important;
}

.el-pagination__sizes .el-input {
  min-width: 240px !important;
  width: auto !important;
}

.el-pagination__sizes .el-input .el-input__wrapper {
  background-color: var(--card-bg) !important;
  border-radius: var(--border-radius) !important;
  min-width: 220px !important;
  width: auto !important;
  overflow: visible !important;
}

.el-pagination__sizes .el-input .el-input__inner {
  padding: 12px 15px !important;
  font-size: 0.875rem !important;
  min-width: 220px !important;
  width: auto !important;
  overflow: visible !important;
  text-overflow: clip !important;
  height: 40px !important;
  line-height: 40px !important;
}

.el-pagination__sizes .el-select-dropdown {
  min-width: 240px !important;
  width: auto !important;
  max-width: none !important;
  z-index: 10000 !important;
  overflow: visible !important;
}

.el-pagination .el-pagination__sizes .el-select .el-select-dropdown .el-select-dropdown__item {
  white-space: nowrap !important;
  padding: 0 20px !important;
  min-width: 220px !important;
  overflow: visible !important;
  text-overflow: clip !important;
  font-size: 14px !important;
  line-height: 44px !important;
  height: 44px !important;
  display: flex !important;
  align-items: center !important;
  justify-content: flex-start !important;
  margin: 0 !important;
  border-bottom: 1px solid var(--border-color) !important;
}

.el-pagination__total {
  margin-right: var(--spacing-lg) !important;
  font-weight: 500 !important;
}

.el-pagination__prev,
.el-pagination__next,
.el-pagination__item {
  margin: 0 var(--spacing-xs) !important;
  border-radius: var(--border-radius) !important;
  transition: var(--transition) !important;
  border: 1px solid var(--border-color) !important;
  background-color: var(--card-bg) !important;
  color: var(--text-color) !important;
  padding: var(--spacing-xs) var(--spacing-md) !important;
}

.el-pagination__prev:hover,
.el-pagination__next:hover,
.el-pagination__item:hover {
  border-color: var(--primary-color) !important;
  color: var(--primary-color) !important;
  transform: translateY(-1px) !important;
  box-shadow: var(--shadow-card) !important;
}

.el-pagination__item.is-current {
  background-color: var(--primary-color) !important;
  border-color: var(--primary-color) !important;
  color: white !important;
  font-weight: 600 !important;
}

.el-pagination__jump {
  margin-left: var(--spacing-lg) !important;
  font-weight: 500 !important;
}

.el-pagination__jump .el-input .el-input__wrapper {
  background-color: var(--card-bg) !important;
  border-radius: var(--border-radius) !important;
}

.el-pagination__jump .el-input .el-input__inner {
  padding: var(--spacing-xs) var(--spacing-sm) !important;
  font-size: 0.875rem !important;
  width: 60px !important;
}

/* 全局对话框样式 */
.el-dialog {
  background-color: var(--card-bg) !important;
  border: 1px solid var(--border-color) !important;
  border-radius: var(--border-radius-lg) !important;
  box-shadow: var(--shadow-card-hover) !important;
  overflow: hidden !important;
}

.el-dialog__header {
  background-color: var(--card-bg) !important;
  border-bottom: 1px solid var(--border-color) !important;
  padding: var(--spacing-lg) !important;
}

.el-dialog__title {
  color: var(--text-color) !important;
  font-weight: 600 !important;
  font-size: 1.2rem !important;
}

.el-dialog__body {
  padding: var(--spacing-lg) !important;
}

.el-dialog__footer {
  background-color: var(--card-bg) !important;
  border-top: 1px solid var(--border-color) !important;
  padding: var(--spacing-lg) !important;
  display: flex !important;
  align-items: center !important;
  justify-content: flex-end !important;
  gap: var(--spacing-md) !important;
}

/* 全局统计组件样式 */
.el-statistic {
  text-align: center !important;
}

.el-statistic__value {
  font-size: 2rem !important;
  font-weight: 700 !important;
  color: var(--text-color) !important;
  margin-bottom: var(--spacing-sm) !important;
}

.el-statistic__label {
  font-size: 0.9rem !important;
  color: var(--text-color-secondary) !important;
  font-weight: 500 !important;
}

/* 全局进度条样式 */
.el-progress {
  margin-bottom: var(--spacing-md) !important;
}

.el-progress__text {
  font-size: 0.875rem !important;
  font-weight: 500 !important;
  color: var(--text-color) !important;
}

.el-progress__bar {
  border-radius: var(--border-radius) !important;
}

.el-progress__bar-inner {
  border-radius: var(--border-radius) !important;
  background-color: var(--primary-color) !important;
  transition: width 0.6s ease !important;
}

/* 全局布局样式 */
.el-container {
  min-height: 100vh !important;
}

.el-header {
  background-color: var(--header-bg) !important;
  color: var(--header-text) !important;
  text-align: center !important;
  line-height: 64px !important;
  font-size: 1.5rem !important;
  font-weight: 700 !important;
  box-shadow: var(--shadow-card) !important;
  position: relative !important;
  z-index: 100 !important;
}

.el-aside {
  background-color: var(--aside-bg) !important;
  padding: var(--spacing-lg) !important;
  min-height: calc(100vh - 64px) !important;
  border-right: 1px solid var(--border-color) !important;
}

.el-main {
  padding: var(--spacing-xl) !important;
  min-height: calc(100vh - 64px) !important;
  overflow-y: auto !important;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .el-aside {
    width: 100% !important;
    min-height: auto !important;
    border-right: none !important;
    border-bottom: 1px solid var(--border-color) !important;
  }
  
  .el-main {
    padding: var(--spacing-md) !important;
  }
  
  .el-header {
    font-size: 1.2rem !important;
    line-height: 56px !important;
  }
  
  .el-menu {
    margin: var(--spacing-md) 0 !important;
  }
  
  .el-menu-item {
    padding: var(--spacing-sm) var(--spacing-md) !important;
  }
  
  .el-card {
    margin-bottom: var(--spacing-md) !important;
  }
  
  .el-table {
    font-size: 0.875rem !important;
  }
  
  .el-table th,
  .el-table td {
    padding: var(--spacing-sm) !important;
  }
  
  .el-pagination {
    flex-direction: column !important;
    gap: var(--spacing-md) !important;
  }
  
  .el-pagination__sizes,
  .el-pagination__jump {
    margin: 0 !important;
  }
}

/* 全局标签样式 */
.el-tag {
  transition: var(--transition) !important;
}

.el-tag:hover {
  transform: translateY(-1px) !important;
  box-shadow: var(--shadow) !important;
}
</style>