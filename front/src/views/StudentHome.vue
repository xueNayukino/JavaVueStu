<template>
  <div class="student-home">
    <!-- 学生照片 -->
    <div class="photo-container">
      <div class="photo-wrapper">
        <img :src="getPhotoUrl(student.photo)" alt="学生照片" class="student-photo" />
      </div>
    </div>

    <!-- 信息表格 -->
    <table class="info-table">
      <tbody>
        <tr>
          <th>姓名</th>
          <td>{{ student.name }}</td>
        </tr>
        <tr>
          <th>性别</th>
          <td>{{ student.gender }}</td>
        </tr>
        <tr>
          <th>年龄</th>
          <td>{{ student.age }}</td>
        </tr>
        <tr>
          <th>入学年份</th>
          <td>{{ student.enrollmentYear }}</td>
        </tr>
        <tr>
          <th>学号</th>
          <td>{{ student.studentId }}</td>
        </tr>
        <tr>
          <th>专业</th>
          <td>{{ student.major }}</td>
        </tr>
        <tr>
          <th>密码</th>
          <td>{{ student.password }}</td>
        </tr>
        <tr>
          <th>报到状态</th>
          <td>{{ student.registrationStatus }}</td>
        </tr>
        <tr>
          <th>电话号码</th>
          <td>{{ student.phoneNumber }}</td>
        </tr>
        <tr>
          <th>身份证</th>
          <td>{{ student.idCard }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "StudentHome",
  data() {
    return {
      student: {
        photo: "student1.jpg",
        name: "",
        gender: "",
        age: "",
        enrollmentYear: "",
        studentId: "",
        major: "",
        password: "",
        dormInfo: "不住校",
        registrationStatus: "",
        phoneNumber: "",
        idCard: "",


      },
    };
  },
  mounted() {
    this.fetchStudentData();
  },
  methods: {
    getPhotoUrl(photo) {
        const baseUrl = 'http://localhost:8112';  // 添加后端服务器地址
        if (!photo || photo === '') {
            return `${baseUrl}/api/student/images/default-avatar.jpg`;
        }
        return `${baseUrl}/api/student/images/${photo}`;
    },

    async fetchStudentData() {
        try {
            const studentId = localStorage.getItem('id');
            console.log('获取到的studentId:', studentId);
            
            if (!studentId) {
                console.error('未找到学生ID');
                this.$router.push('/');
                return;
            }

            const response = await axios.get(`/api/student/home?id=${studentId}`);
            console.log('服务器响应:', response.data);

            if (response.data.success) {
                const studentData = response.data.result.student;
                if (studentData) {
                    // 格式化日期
                    if (studentData.enrollmentYear) {
                        const date = new Date(studentData.enrollmentYear);
                        studentData.enrollmentYear = date.toISOString().split('T')[0];
                    }
                    
                    // 更新学生数据
                    this.student = {
                        ...this.student,
                        ...studentData
                    };
                }
            } else {
                console.error("获取学生信息失败:", response.data.message);
                this.$router.push('/');
            }
        } catch (error) {
            console.error("获取学生信息失败:", error);
            this.$router.push('/');
        }
    }
  },
};
</script>

<style scoped>
.student-home {
  max-width: 600px;
  margin: 0 auto;
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.photo-container {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

.photo-wrapper {
  border: 2px solid #000;
  padding: 5px;
  border-radius: 8px;
}

.student-photo {
  width: 150px;
  height: 150px;
  border-radius: 8px;
  object-fit: cover;
}

.info-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  border: 1px solid #ddd;
}

.info-table th,
.info-table td {
  border: 1px solid #ddd;
  padding: 10px;
  text-align: left;
  vertical-align: top;
}

.info-table th {
  background-color: #f0f0f0;
  width: 150px; /* 设置表头宽度 */
}

.info-table td {
  width: 200px; /* 设置单元格宽度 */
}
</style>
