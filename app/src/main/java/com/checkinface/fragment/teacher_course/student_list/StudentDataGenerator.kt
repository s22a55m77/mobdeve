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
            data.add(
                StudentModel(
                    "Student 5",
                    "student5@gmail.com",
                    3,
                    3,
                    0
                )
            )
            data.add(
                StudentModel(
                    "Student 6",
                    "student6@gmail.com",
                    2,
                    4,
                    1
                )
            )
            data.add(
                StudentModel(
                    "Student 7",
                    "student7@gmail.com",
                    5,
                    1,
                    2
                )
            )
            data.add(
                StudentModel(
                    "Student 8",
                    "student8@gmail.com",
                    4,
                    2,
                    3
                )
            )
            data.add(
                StudentModel(
                    "Student 9",
                    "student9@gmail.com",
                    3,
                    3,
                    2
                )
            )
            data.add(
                StudentModel(
                    "Student 10",
                    "student10@gmail.com",
                    4,
                    1,
                    1
                )
            )
            data.add(
                StudentModel(
                    "Student 11",
                    "student11@gmail.com",
                    2,
                    2,
                    2
                )
            )
            data.add(
                StudentModel(
                    "Student 12",
                    "student12@gmail.com",
                    3,
                    1,
                    3
                )
            )
            data.add(
                StudentModel(
                    "Student 13",
                    "student13@gmail.com",
                    5,
                    0,
                    2
                )
            )
            data.add(
                StudentModel(
                    "Student 14",
                    "student14@gmail.com",
                    1,
                    3,
                    1
                )
            )
            return data
        }
    }
}