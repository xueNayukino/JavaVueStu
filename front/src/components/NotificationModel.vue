<template>
    <div v-if="visible" class="modal">
      <div class="modal-content">
        <button class="close-btn" @click="close">×</button>
        <h2>编辑通知</h2>
  
        <!-- 编辑标题 -->
        <label for="title">标题：</label>
        <input
          id="title"
          type="text"
          v-model="editableNotification.title"
          class="input-field"
        />
  
        <!-- 编辑内容 -->
        <label for="content">内容：</label>
        <textarea
          id="content"
          v-model="editableNotification.content"
          rows="4"
          class="textarea-field"
        ></textarea>
  
        <div class="modal-actions">
          <button class="btn btn-save" @click="saveChanges">保存更改</button>
          <button class="btn btn-cancel" @click="close">取消</button>
        </div>
      </div>
    </div>
  </template>
  
  <script>
  export default {
    props: {
      notification: {
        type: Object,
        required: true
      },
      visible: {
        type: Boolean,
        required: true
      }
    },
    data() {
      return {
        editableNotification: {
          id: null,
          title: '',
          content: ''
        }
      }
    },
    watch: {
      notification(newVal) {
        if (newVal) {
          this.editableNotification = {
            fileId: newVal.fileId,
            title: newVal.fileTitle,
            content: newVal.fileContent
          }
        }
      }
    },
    methods: {
      close() {
        this.$emit("close");
      },
      saveChanges() {
        this.$emit('edit', {
          fileId: this.editableNotification.fileId,
          title: this.editableNotification.title,
          content: this.editableNotification.content
        });
      },
    },
  };
  </script>
  
  <style scoped>
  /* 样式与之前一致，仅添加表单字段样式 */
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
  
  .input-field,
  .textarea-field {
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 6px;
    margin-bottom: 15px;
    font-size: 14px;
  }
  
  .input-field:focus,
  .textarea-field:focus {
    outline: none;
    border-color: #1890ff;
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
  
  .btn-save {
    background: #1890ff;
    color: white;
  }
  
  .btn-save:hover {
    background: #1274d2;
  }
  
  .btn-cancel {
    background: #ccc;
    color: black;
  }
  
  .btn-cancel:hover {
    background: #aaa;
  }
  </style>
  