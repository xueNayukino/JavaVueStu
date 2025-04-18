<template>
  <div v-if="isVisible" class="add-notification-modal">
    <div class="modal-content">
      <h2>新建通知</h2>
      <form @submit.prevent="submitForm">
        <div class="form-group">
          <label for="title">通知标题</label>
          <input 
            type="text" 
            id="title" 
            v-model.trim="notification.fileTitle" 
            required
            placeholder="请输入通知标题"
          />
        </div>
        <div class="form-group">
          <label for="content">通知内容</label>
          <textarea 
            id="content" 
            v-model.trim="notification.fileContent" 
            required
            placeholder="请输入通知内容"
          ></textarea>
        </div>
        <div class="button-group">
          <button type="submit" class="submit-btn">提交</button>
          <button type="button" @click="cancel" class="cancel-btn">取消</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import { ElMessage } from 'element-plus'

export default {
  name: 'AddNotification',
  props: {
    isVisible: {
      type: Boolean,
      required: true
    }
  },
  data() {
    return {
      notification: {
        fileTitle: '',
        fileContent: '',
        publishDate: null,
        modifyDate: null,
        viewCount: 0
      }
    }
  },
  methods: {
    submitForm() {
      console.log('提交的表单数据:', this.notification); // 添加日志

      // 表单验证
      if (!this.notification.fileTitle || !this.notification.fileContent) {
        ElMessage({
          type: 'warning',
          message: '请填写完整的通知信息'
        });
        return;
      }

      // 设置日期
      const now = new Date();
      this.notification.publishDate = now;
      this.notification.modifyDate = now;

      // 发送数据到父组件
      console.log('发送到父组件的数据:', this.notification); // 添加日志
      this.$emit('add', { ...this.notification }); // 使用解构以避免引用问题
      
      // 清空表单
      this.resetForm();
      
      // 关闭模态框
      this.$emit('update:isVisible', false);
    },
    cancel() {
      // 清空表单并关闭模态框
      this.resetForm();
      this.$emit('update:isVisible', false);
    },
    resetForm() {
      this.notification = {
        fileTitle: '',
        fileContent: '',
        publishDate: null,
        modifyDate: null,
        viewCount: 0
      };
    }
  },
  watch: {
    isVisible(newVal) {
      if (newVal) {
        // 当模态框打开时，重置表单
        this.resetForm();
      }
    }
  }
}
</script>

<style scoped>
.add-notification-modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  width: 80%;
  max-width: 500px;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input, textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

textarea {
  height: 150px;
  resize: vertical;
}

.button-group {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.submit-btn {
  background-color: #4CAF50;
  color: white;
}

.cancel-btn {
  background-color: #f44336;
  color: white;
}
</style>
  