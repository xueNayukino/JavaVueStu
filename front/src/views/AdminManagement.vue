<template>
  <div class="student-management">
    <!-- 搜索功能 -->
    <div class="search-container">
      <input type="text" v-model="searchText" placeholder="请输入要查询的学生学号" class="search-input" />
      <button @click="searchStudent" class="search-button">搜索</button>
    </div>

    <table class="student-table">
      <thead>
        <tr>
          <th>学号</th>
          <th>姓名</th>
          <th>性别</th>
          <th>电话</th>
          <th>报到状态</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="student in filterList.length > 0 ? filterList : OriginList" :key="student.studentId">
          <td>{{ student.studentId }}</td>
          <td>{{ student.name }}</td>
          <td>{{ student.gender }}</td>
          <td>{{ student.phoneNumber }}</td>
          <td>{{ student.registrationStatus }}</td>
          <td>
            <button @click="viewDetail(student)" class="detail-button">详情</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- 子组件：学生详情弹窗 -->
    <AdminStudentDetail v-if="showDetail" :student="selectedStudent" @update-student="updateStudent"
      @delete-student="deleteStudent" @close="closeDetail" />
  </div>
</template>

<script>
import axios from "axios";
import AdminStudentDetail from "@/components/AdminStudentDetail.vue";
import { ElMessage } from 'element-plus'
import { ref, watch } from 'vue'

export default {
  setup() {
    const OriginList = ref([])
    const filterList = ref([])

    // 监听原始列表的变化
    watch(OriginList, (newVal) => {
      console.log('OriginList updated:', newVal)
    }, { deep: true })

    return {
      OriginList,
      filterList
    }
  },
  data() {
    return {
      searchText: "", // 搜索关键字
      showDetail: false, // 是否显示详情弹窗
      selectedStudent: null, // 当前选中的学生
    };
  },
  components: {
    AdminStudentDetail,
  },
  methods: {
    // 初始化获取学生数据
    async fetchStudents() {
      try {
        const response = await axios.get("/api/admin/getStudents");
        if (response.data.success) {
          this.OriginList = response.data.data; // 从后端获取学生数据
        } else {
          console.error("获取学生数据失败");
        }
      } catch (error) {
        console.error("接口调用失败：", error);
      }
    },

    // 搜索学生
    searchStudent() {
      const searchValue = this.searchText.trim().toLowerCase();
      if (!searchValue) {
        this.filterList = [];
        return;
      }
      this.filterList = this.OriginList.filter(
        (student) =>
          student.studentId.toString().includes(searchValue) ||
          student.name.toLowerCase().includes(searchValue)
      );
    },

    // 查看学生详情
    viewDetail(student) {
      this.selectedStudent = student;
      this.showDetail = true;
    },

    // 关闭详情弹窗
    closeDetail() {
      this.showDetail = false;
      this.selectedStudent = null;
    },

    // 更新学生信息
    async updateStudent(updatedStudent) {
      try {
        const response = await axios.post("/api/admin/updateStudent", updatedStudent);
        console.log('服务器返回的数据:', response.data);
        
        if (response.data.success) {
            // 找到要更新的学生索引
            const index = this.OriginList.findIndex(
                (student) => student.studentId === updatedStudent.studentId
            );
            
            if (index !== -1) {
                // 保持原有数据结构，只更新修改的字段
                this.OriginList[index] = {
                    ...this.OriginList[index],
                    ...response.data.data,
                    registrationStatus: response.data.data.registrationStatus
                };
                
                // 如果有筛选列表，也需要更新
                const filterIndex = this.filterList.findIndex(
                    (student) => student.studentId === updatedStudent.studentId
                );
                if (filterIndex !== -1) {
                    this.filterList[filterIndex] = this.OriginList[index];
                }
            }
            
            ElMessage({
                type: 'success',
                message: '更新成功'
            });
            this.closeDetail();
        } else {
            ElMessage({
                type: 'error',
                message: response.data.message || '更新失败'
            });
        }
      } catch (error) {
        console.error("更新请求失败:", error);
        ElMessage({
            type: 'error',
            message: error.response?.data?.message || '更新失败：' + error.message
        });
      }
    },

    // 删除学生信息
    async deleteStudent(studentId) {
      try {
        if (!studentId) {
          console.error("学生ID不能为空");
          return;
        }
        const response = await axios.delete(`/api/admin/deleteStudent/${studentId}`);
        if (response.data.success) {
          this.OriginList = this.OriginList.filter(
            (student) => student.studentId !== studentId
          ); // 删除本地数据
          this.closeDetail();
          ElMessage({
            type: 'success',
            message: '删除成功'
          });
        } else {
          console.error("删除学生信息失败");
          ElMessage({
            type: 'error',
            message: '删除失败'
          });
        }
      } catch (error) {
        console.error("接口调用失败：", error);
        ElMessage({
          type: 'error',
          message: '删除失败：' + error.message
        });
      }
    },
  },
  mounted() {
    this.fetchStudents(); // 初始化获取学生数据
  },
};
</script>


<style scoped>
.student-management {
  margin: 20px;
}

.search-container {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.search-input {
  flex: 1;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.search-button {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.search-button:hover {
  background-color: #0056b3;
}

.student-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.student-table th,
.student-table td {
  border: 1px solid #ddd;
  padding: 8px;
  text-align: center;
}

.student-table th {
  background-color: #f4f4f4;
}

.detail-button {
  padding: 5px 10px;
  background-color: #28a745;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.detail-button:hover {
  background-color: #218838;
}
</style>