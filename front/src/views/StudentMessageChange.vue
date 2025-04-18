<template>
  <div>
    <el-form label-position="top" label-width="120px" class="student-form" :model="student">
      <!-- 学生照片 -->
      <el-form-item label="照片">
        <el-upload
          class="avatar-uploader"
          :action="uploadUrl"
          :data="uploadData"
          :show-file-list="false"
          :before-upload="beforeUpload"
          :on-success="handleUploadSuccess"
          :on-error="handleUploadError">
          <img v-if="student.photo" :src="getPhotoUrl(student.photo)" class="student-photo">
          <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
        </el-upload>
      </el-form-item>

      <!-- 学生姓名 -->
      <el-form-item label="姓名">
        <el-input v-model="student.name"></el-input>
      </el-form-item>

      <!-- 学生性别 -->
      <el-form-item label="性别">
        <el-select v-model="student.gender" placeholder="请选择性别">
          <el-option label="男" value="男"></el-option>
          <el-option label="女" value="女"></el-option>
        </el-select>
      </el-form-item>


      <el-form-item label="年龄">
        <el-input v-model="student.age"></el-input>
      </el-form-item>

      <!-- 入学年份 -->
      <el-form-item label="入学年份">
        <el-date-picker
          v-model="student.enrollmentYear"
          type="date"
          placeholder="选择入学年份"
          format="YYYY-MM-DD"
          value-format="YYYY-MM-DD"
        ></el-date-picker>
      </el-form-item>

      <!-- 电话号码 -->
      <el-form-item label="电话号码">
        <el-input v-model="student.phoneNumber"></el-input>
      </el-form-item>

      <!-- 学号 -->
      <el-form-item label="学号">
        <el-input v-model="student.studentId"></el-input>
      </el-form-item>

      <!-- 专业 -->
      <el-form-item label="专业">
        <el-input v-model="student.major"></el-input>
      </el-form-item>

      <!-- 密码 -->
      <el-form-item label="密码">
        <el-input type="password" v-model="student.password"></el-input>
      </el-form-item>
      <!-- 报道信息 -->
      <el-form-item label="报道信息">
        <el-select v-model="student.registrationStatus" placeholder="请选择报道状态">
          <el-option label="已报道" value="已报道"></el-option>
          <el-option label="未报道" value="未报道"></el-option>
        </el-select>
      </el-form-item>

      <!-- 身份证 -->
      <el-form-item label="身份证">
        <el-input v-model="student.idCard"></el-input>
      </el-form-item>

      <!-- 提交按钮 -->
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import axios from 'axios';
import { Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus';

export default {
  name: 'StudentMessageChange',
  components: {
    Plus
  },
  data() {
    return {
      uploadUrl: 'http://localhost:8112/api/student/upload/photo',  // 修改为正确的后端地址
      student: {
        photo: '',
        name: '',
        gender: '',
        age: '',
        enrollmentYear: '',
        studentId: '',
        major: '',
        password: '',
        registrationStatus: '未报道',
        phoneNumber: '',
        idCard: '',
      },
      uploadData: {
        studentId: ''  // 将在 mounted 中设置
      }
    };
  },
  mounted() {
    // 设置学生ID
    this.uploadData.studentId = localStorage.getItem('id');
    this.getStudentData();
  },
  methods: {
    beforeUpload(file) {
      const isImage = file.type.startsWith('image/');
      const isLt5M = file.size / 1024 / 1024 < 5;

      if (!isImage) {
        ElMessage.error('只能上传图片文件!');
        return false;
      }
      if (!isLt5M) {
        ElMessage.error('图片大小不能超过 5MB!');
        return false;
      }
      return true;
    },

    handleUploadSuccess(response) {
      console.log('上传响应:', response);
      if (response.success) {
        this.student.photo = response.result.url;
        // 强制刷新图片
        this.$nextTick(() => {
          const img = document.querySelector('.student-photo');
          if (img) {
            img.src = this.getPhotoUrl(response.result.url) + '?t=' + new Date().getTime();
          }
        });
        ElMessage.success('上传成功');
      } else {
        ElMessage.error(response.message || '上传失败');
      }
    },

    handleUploadError(error) {
      console.error('上传错误:', error);
      ElMessage.error('上传失败：' + (error.message || '未知错误'));
    },

    getPhotoUrl(photo) {
      const baseUrl = 'http://localhost:8112';  // 直接使用硬编码的baseUrl
      if (!photo || photo === '') {
        return `${baseUrl}/api/student/images/default-avatar.jpg`;
      }
      return `${baseUrl}/api/student/images/${photo}`;
    },

    // 获取学生数据
    async getStudentData() {
      try {
        const studentId = localStorage.getItem('id');
        const response = await axios.get(`/api/student/change?id=${studentId}`);
        if (response.data.success) {
          const data = response.data.result;
          // 格式化日期
          if (data.enrollmentYear) {
            const date = new Date(data.enrollmentYear);
            data.enrollmentYear = date.toISOString().split('T')[0];
          }
          this.student = data;
        }
      } catch (error) {
        console.error('获取学生数据失败:', error);
        ElMessage.error('获取学生数据失败');
      }
    },

    // 提交表单
    async submitForm() {
      try {
        const studentId = localStorage.getItem('id');
        if (!studentId) {
          console.error('未找到学生ID');
          return;
        }

        // 构建更新数据
        const updateData = {
          studentId: parseInt(studentId),
          name: this.student.name,
          gender: this.student.gender,
          age: this.student.age ? parseInt(this.student.age) : null,
          enrollmentYear: this.student.enrollmentYear, // 直接使用日期选择器的值
          phoneNumber: this.student.phoneNumber,
          major: this.student.major,
          password: this.student.password,
          registrationStatus: this.student.registrationStatus || '未报道',
          idCard: this.student.idCard,
          photo: this.student.photo
        };

        console.log('提交的数据:', updateData);

        const response = await axios.put(`/api/student/update/${studentId}`, updateData);
        
        if (response.data.success) {
          ElMessage.success('更新成功');
          await this.getStudentData();
        } else {
          ElMessage.error(response.data.message || '更新失败');
        }
      } catch (error) {
        console.error('提交失败:', error);
        ElMessage.error(error.response?.data?.message || '提交失败');
      }
    }
  }
};
</script>

<style scoped>
.student-form {
  max-width: 400px;
  margin: 0 auto; /* 居中 */
  background-color: #f9f9f9; /* 浅灰背景 */
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1); /* 添加阴影 */
}
.student-photo {
  width: 150px;
  height: 150px;
  border-radius: 8px;
  object-fit: cover; /* 确保图片不变形 */
  margin-bottom: 10px;
}

.avatar-uploader {
  text-align: center;
  margin-bottom: 20px;
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 150px;
  height: 150px;
  line-height: 150px;
  text-align: center;
  border: 1px dashed #d9d9d9;
  border-radius: 8px;
}

.avatar-uploader:hover .avatar-uploader-icon {
  border-color: #409EFF;
  color: #409EFF;
}
</style>
