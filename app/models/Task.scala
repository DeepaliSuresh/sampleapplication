package models

import play.api.libs.json.Json

import java.time.LocalDate

case class Task(
                 Id: Long,
                 TaskId: String,
                 Owner: String,
                 Assignee: String,
                 EstimatedDate: LocalDate,
                 Status: String,
                 CreatedDate: LocalDate,
                 UpdatedDate: LocalDate,
                 Flag: Boolean
               )

