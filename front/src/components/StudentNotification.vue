<template>
    <div v-if="visible" class="modal">
      <div class="modal-content">
        <button class="close-btn" @click="close">×</button>
        <h2>查看通知</h2>
  
        <!-- 显示标题 -->
        <label for="title">标题：</label>
        <div id="title" class="display-field">{{ editableNotification.title }}</div>
  
        <!-- 显示内容 -->
        <label for="content">内容：</label>
        <div id="content" class="display-field">{{ editableNotification.content }}</div>
  
        <div class="modal-actions">
          <button class="btn btn-cancel" @click="close">关闭</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      visible: {
        type: Boolean,
        required: true,
      },
      notification: {
        type: Object,
        default: () => ({}),
      },
    },
    emits: ["close"],
    data() {
      return {
        editableNotification: { ...this.notification }, // 深拷贝传入的数据，避免直接修改父组件数据
      };
    },
    methods: {
      close() {
        this.$emit("close");
      },
    },
    watch: {
      // 监听传入的 notification，每次弹框打开时同步数据
      notification: {
        immediate: true,
        handler(newNotification) {
          this.editableNotification = { ...newNotification };
        },
      },
    },
  };
  </script>
  
  <style scoped>
  /* 样式与之前一致，仅更改为显示数据 */
  .modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.6);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 9999;
  }
  
  .modal-content {
    position: relative;
    background: #fff;
    padding: 30px 20px;
    border-radius: 12px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.3);
    width: 400px;
    text-align: left;
  }
  
  .close-btn {
    position: absolute;
    top: 10px;
    right: 10px;
    background: none;
    border: none;
    font-size: 20px;
    color: #888;
    cursor: pointer;
  }
  
  label {
    display: block;
    font-weight: bold;
    margin: 10px 0 5px;
  }
  
  .display-field {
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 6px;
    margin-bottom: 15px;
    font-size: 14px;
    background-color: #f9f9f9;
  }
  
  .modal-actions {
    display: flex;
    justify-content: flex-end;
    gap: 10px;
  }
  
  .btn {
    padding: 8px 16px;
    border: none;
    border-radius: 6px;
    font-size: 14px;
    cursor: pointer;
    transition: all 0.3s ease;
  }
  
  .btn-cancel {
    background: #ccc;
    color: black;
  }
  
  .btn-cancel:hover {
    background: #aaa;
  }
  </style>
  