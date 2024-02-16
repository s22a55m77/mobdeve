package com.checkinface.fragment.teacher_course.student_list

class StudentDataGenerator {
    companion object {
        fun loadData(): ArrayList<StudentModel> {
            val data = ArrayList<StudentModel>()
            data.add(
                StudentModel(
                    "Student 1",
                    "student1@gmail.com",
                    1,
                    2,
                    3
                )
            )
            data.add(
                StudentModel(
                    "Student 2",
                    "student2@gmail.com",
                    6,
                    0,
                    0
                )
            )
            data.add(
                StudentModel(
                    "Student 3",
                    "student3@gmail.com",
                    5,
                    1,
                    0
                )
            )
            data.add(
                StudentModel(
                    "Student 4",
                    "student4@gmail.com",
                    4,
                    2,
                    1
                )
            )

            return data
        }
    }
}