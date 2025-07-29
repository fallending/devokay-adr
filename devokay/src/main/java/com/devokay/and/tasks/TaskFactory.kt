package com.devokay.and.tasks

import com.effective.android.anchors.task.project.Project.TaskFactory
import com.effective.android.anchors.task.Task
import com.effective.android.anchors.task.TaskCreator
import java.util.*

abstract class TestTask(
  id: String,
  isAsyncTask: Boolean = false //是否是异步存在
) : Task(id, isAsyncTask) {

  fun doIo(millis: Long) {
    try {
      Thread.sleep(millis)
    } catch (e: Exception) {
    }
  }

  fun doJob(millis: Long) {
    val nowTime = System.currentTimeMillis()
    while (System.currentTimeMillis() < nowTime + millis) {
      //程序阻塞指定时间
      val min = 10
      val max = 99
      val random = Random()
      val num = random.nextInt(max) % (max - min + 1) + min
    }
  }
}

class TASK_10 : TestTask(TaskData.TASK_10, true) {
  override fun run(name: String) {
    doJob(1000)
  }
}

class TASK_11 : TestTask(TaskData.TASK_11, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_12 : TestTask(TaskData.TASK_12, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_13 : TestTask(TaskData.TASK_13, false) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_20 : TestTask(TaskData.TASK_20, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_21 : TestTask(TaskData.TASK_21, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_22 : TestTask(TaskData.TASK_22, true) {
  override fun run(name: String) {
    doIo(200)
  }
}

class TASK_23 : TestTask(TaskData.TASK_23, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_30 : TestTask(TaskData.TASK_30, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_31 : TestTask(TaskData.TASK_31, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_32 : TestTask(TaskData.TASK_32, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_33 : TestTask(TaskData.TASK_33, true) {
  override fun run(name: String) {
    doIo(200)
  }
}

class TASK_40 : TestTask(TaskData.TASK_40, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_41 : TestTask(TaskData.TASK_41, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_42 : TestTask(TaskData.TASK_42, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_43 : TestTask(TaskData.TASK_43, true) {
  override fun run(name: String) {
    doIo(200)
  }
}

class TASK_50 : TestTask(TaskData.TASK_50, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_51 : TestTask(TaskData.TASK_51, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_52 : TestTask(TaskData.TASK_52, true) {
  override fun run(name: String) {
    doIo(200)
  }
}

class TASK_53 : TestTask(TaskData.TASK_53, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_60 : TestTask(TaskData.TASK_60, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_61 : TestTask(TaskData.TASK_61, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_62 : TestTask(TaskData.TASK_62, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_63 : TestTask(TaskData.TASK_63, true) {
  override fun run(name: String) {
    doIo(200)
  }
}

class TASK_70 : TestTask(TaskData.TASK_70, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_71 : TestTask(TaskData.TASK_71, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_72 : TestTask(TaskData.TASK_72, true) {
  override fun run(name: String) {
    doIo(200)
  }
}

class TASK_73 : TestTask(TaskData.TASK_73, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_80 : TestTask(TaskData.TASK_80, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_81 : TestTask(TaskData.TASK_81, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_82 : TestTask(TaskData.TASK_82, true) {
  override fun run(name: String) {
    doIo(200)
  }
}

class TASK_83 : TestTask(TaskData.TASK_83, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_90 : TestTask(TaskData.TASK_90, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_91 : TestTask(TaskData.TASK_91, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_92 : TestTask(TaskData.TASK_92) {
  override fun run(name: String) {
    doIo(200)
  }
}

class TASK_93 : TestTask(TaskData.TASK_93, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_100 : TestTask(TaskData.TASK_100, true) {
  override fun run(name: String) {
    doJob(200)
  }

}

class TASK_101 : TestTask(TaskData.TASK_101, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class TASK_102 : TestTask(TaskData.TASK_102) {
  override fun run(name: String) {
    doIo(200)
  }

}

class TASK_103 : TestTask(TaskData.TASK_103, true) {
  override fun run(name: String) {
    doJob(200)
  }

}

class CUTOUT_TASK_1 : TestTask(TaskData.CUTOUT_TASK_1, true) {
  override fun run(name: String) {
    doJob(200)
  }

  override fun modifySons(behindTaskIds: Array<String>): Array<String> {
    if (behindTaskIds.size >= 3) {
      return arrayOf(behindTaskIds[0], behindTaskIds[2])
    }
    return behindTaskIds
  }

}

class UITHREAD_TASK_A : TestTask(TaskData.UITHREAD_TASK_A) {
  override fun run(name: String) {
    doJob(200)
  }
}

class UITHREAD_TASK_B : TestTask(TaskData.UITHREAD_TASK_B) {
  override fun run(name: String) {
    doJob(200)
  }
}

class UITHREAD_TASK_C : TestTask(TaskData.UITHREAD_TASK_C) {
  override fun run(name: String) {
    doJob(200)
  }
}

class ASYNC_TASK_1 : TestTask(TaskData.ASYNC_TASK_1, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class ASYNC_TASK_2 : TestTask(TaskData.ASYNC_TASK_2, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class ASYNC_TASK_3 : TestTask(TaskData.ASYNC_TASK_3, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class ASYNC_TASK_4 : TestTask(TaskData.ASYNC_TASK_4, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

class ASYNC_TASK_5 : TestTask(TaskData.ASYNC_TASK_5, true) {
  override fun run(name: String) {
    doJob(200)
  }
}

object TestTaskCreator : TaskCreator {
  override fun createTask(taskName: String): Task {
    when (taskName) {
      TaskData.TASK_10 -> {
        val task_10 = TASK_10()
        task_10.priority = 10
        return task_10
      }
      TaskData.TASK_11 -> {
        val TASK_11 = TASK_11()
        TASK_11.priority = 10
        return TASK_11
      }
      TaskData.TASK_12 -> {
        val TASK_12 = TASK_12()
        TASK_12.priority = 10
        return TASK_12
      }
      TaskData.TASK_13 -> {
        val TASK_13 = TASK_13()
        TASK_13.priority = 10
        return TASK_13
      }
      TaskData.TASK_20 -> {
        return TASK_20()
      }
      TaskData.TASK_21 -> {
        return TASK_21()
      }
      TaskData.TASK_22 -> {
        return TASK_22()
      }
      TaskData.TASK_23 -> {
        return TASK_23()
      }
      TaskData.TASK_30 -> {
        return TASK_30()
      }
      TaskData.TASK_31 -> {
        return TASK_31()
      }
      TaskData.TASK_32 -> {
        return TASK_32()
      }
      TaskData.TASK_33 -> {
        return TASK_33()
      }
      TaskData.TASK_40 -> {
        return TASK_40()
      }
      TaskData.TASK_41 -> {
        return TASK_41()
      }
      TaskData.TASK_42 -> {
        return TASK_42()
      }
      TaskData.TASK_43 -> {
        return TASK_43()
      }
      TaskData.TASK_50 -> {
        return TASK_50()
      }
      TaskData.TASK_51 -> {
        return TASK_51()
      }
      TaskData.TASK_52 -> {
        return TASK_52()
      }
      TaskData.TASK_53 -> {
        return TASK_53()
      }
      TaskData.TASK_60 -> {
        return TASK_60()
      }
      TaskData.TASK_61 -> {
        return TASK_61()
      }
      TaskData.TASK_62 -> {
        return TASK_62()
      }
      TaskData.TASK_63 -> {
        return TASK_63()
      }
      TaskData.TASK_70 -> {
        return TASK_70()
      }
      TaskData.TASK_71 -> {
        return TASK_71()
      }
      TaskData.TASK_72 -> {
        return TASK_72()
      }
      TaskData.TASK_73 -> {
        return TASK_73()
      }
      TaskData.TASK_80 -> {
        return TASK_80().apply { priority = 30 }
      }
      TaskData.TASK_81 -> {
        return TASK_81().apply { priority = 40 }
      }
      TaskData.TASK_82 -> {
        return TASK_82()
      }
      TaskData.TASK_83 -> {
        return TASK_83().apply { priority = 20 }
      }
      TaskData.TASK_90 -> {
        return TASK_90()
      }
      TaskData.TASK_91 -> {
        return TASK_91()
      }
      TaskData.TASK_92 -> {
        return TASK_92()
      }
      TaskData.TASK_93 -> {
        return TASK_93()
      }
      TaskData.TASK_100 -> {
        return TASK_100().apply { priority = 20 }
      }
      TaskData.TASK_101 -> {
        return TASK_101()
      }
      TaskData.TASK_102 -> {
        return TASK_102()
      }
      TaskData.TASK_103 -> {
        return TASK_103()
      }
      TaskData.CUTOUT_TASK_1 -> {
        return CUTOUT_TASK_1()
      }
      TaskData.UITHREAD_TASK_A -> {
        return UITHREAD_TASK_A()
      }
      TaskData.UITHREAD_TASK_B -> {
        return UITHREAD_TASK_B()
      }
      TaskData.UITHREAD_TASK_C -> {
        return UITHREAD_TASK_C()
      }
      TaskData.ASYNC_TASK_1 -> {
        return ASYNC_TASK_1()
      }
      TaskData.ASYNC_TASK_2 -> {
        return ASYNC_TASK_2()
      }
      TaskData.ASYNC_TASK_3 -> {
        return ASYNC_TASK_3()
      }
      TaskData.ASYNC_TASK_4 -> {
        return ASYNC_TASK_4()
      }
      TaskData.ASYNC_TASK_5 -> {
        return ASYNC_TASK_5()
      }
    }
    return ASYNC_TASK_5()
  }
}

class TestTaskFactory : TaskFactory(TestTaskCreator)
