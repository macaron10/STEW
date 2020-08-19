<template>
  <v-row class="fill-height">
    <v-col>
      <v-sheet height="64">
        <v-toolbar flat color="white">
          <v-menu bottom right>
            <template v-slot:activator="{ on, attrs }">
              <v-btn outlined color="grey darken-2" v-bind="attrs" v-on="on" style="width:90px">
                <span>{{ typeToLabel[type] }}</span>
                <v-icon right>mdi-menu-down</v-icon>
              </v-btn>
            </template>
            <v-list>
              <v-list-item @click="type = 'day'">
                <v-list-item-title>Day</v-list-item-title>
              </v-list-item>
              <v-list-item @click="type = 'week'">
                <v-list-item-title>Week</v-list-item-title>
              </v-list-item>
              <v-list-item @click="type = 'month'">
                <v-list-item-title>Month</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
          
          <v-btn fab text small color="grey darken-2" @click="prev">
            <v-icon small>mdi-chevron-left</v-icon>
          </v-btn>
          <v-toolbar-title  v-if="$refs.calendar" @click="setToday">
            {{ $refs.calendar.title }}
          </v-toolbar-title>
           <v-btn fab text small color="grey darken-2" @click="next">
            <v-icon small>mdi-chevron-right</v-icon>
          </v-btn>
          <v-spacer></v-spacer>
          <!-- <v-btn outlined color="grey darken-2">NEW</v-btn> -->

          <v-dialog v-model="dialog" max-width="500" id="box" style="padding: 10px">
            <v-card class="px-5 pt-5 pb-1">
              <v-card-title class="headline"></v-card-title>
              <v-date-picker
                color="blue lighten-2"
                v-model="newSchedule.dates"
                no-title full-width range
              ></v-date-picker>
              <v-col>
                <v-radio-group v-model="newSchedule.color" row >
                  <v-radio v-for="color in colors" :value="color.value" :key="color.value">
                      <template v-slot:label>
                        <v-icon :color="color.value">mdi-checkbox-blank-circle</v-icon>
                      </template>
                  </v-radio>
                </v-radio-group>
                <v-row>
                  <v-col cols=8 class="py-0">
                    <v-text-field label="기간" disabled v-model="dateRangeText"></v-text-field>
                  </v-col>
                  <v-col cols=4 class="py-0">
                    <v-checkbox v-model="newSchedule.useTime" class="ma-4" label="시간"></v-checkbox>
                  </v-col>
                </v-row>
                <v-row class="ma-0">
                  <v-col cols=5 class="pa-0 pr-5">
                    <v-text-field
                      v-if="newSchedule.useTime"
                      label="시작 시간"
                      v-model="newSchedule.startTime"
                      width="50%"
                      type="time"
                    ></v-text-field>
                  </v-col>
                  <v-col cols=5 class="pa-0 pr-5">
                    <v-text-field
                      v-if="newSchedule.useTime"
                      label="종료 시간"
                      v-model="newSchedule.endTime"
                      width="50%"
                      type="time"
                    ></v-text-field>
                  </v-col>
                </v-row>
                <v-text-field label="제목" v-model="newSchedule.name" class="ma-0 mr-2"></v-text-field>
                <v-text-field label="설명" v-model="newSchedule.details" class="ma-0 mr-2"></v-text-field>
                <v-row class="d-flex justify-end">
                  <v-btn depressed dark color="#2b90d9" class="mx-3" @click="createNewSchedule">일정추가</v-btn>
                  <v-btn depressed dark color="grey lighten-1" class="mx-3" @click="reset">초기화</v-btn>
                </v-row>
              </v-col>
              

            </v-card>
          </v-dialog>
          <v-spacer></v-spacer>
          <v-btn fab small outlined dark color="indigo" @click.stop="newForm()">
            <v-icon dark>mdi-pencil</v-icon>
          </v-btn>
          
        </v-toolbar>
      </v-sheet>
      <v-sheet height="600">
        <v-calendar
          ref="calendar"
          v-model="focus"
          color="primary"
          :events="events"
          :event-color="getEventColor"
          :type="type"
          @click:event="showEvent"
          @click:more="viewDay"
          @click:date="viewDay"
          @change="updateRange"
        ></v-calendar>
        <v-menu
          v-model="selectedOpen"
          :close-on-content-click="false"
          :activator="selectedElement"
          offset-x
        >
          <v-card color="grey lighten-4" min-width="350px" flat>
            <v-toolbar :color="selectedEvent.color" dark>
              <v-toolbar-title v-html="selectedEvent.name"></v-toolbar-title>
              <v-spacer></v-spacer>
              <v-btn
                @click="deleteSchedule(selectedEvent.pk, selectedEvent.type)"
                @change="updateRange"
                icon
                v-if="(selectedEvent.type==='U' && this.private) || (selectedEvent.type==='G' && !this.private)"
              >
                <v-icon>mdi-trash-can</v-icon>
              </v-btn>
            </v-toolbar>
            <v-card-text>
              <p>{{scheduleTypeResult}}</p>

              <span>{{ selectedEvent.start | dateToString(selectedEvent.timed, true) }} ~ {{selectedEvent.end | dateToString(selectedEvent.timed, false) }}</span>
              <hr />
              <span v-html="selectedEvent.details"></span>
            </v-card-text>
            <v-card-actions>
              <v-btn text color="secondary" @click="selectedOpen = false">Cancel</v-btn>
            </v-card-actions>
          </v-card>
        </v-menu>
      </v-sheet>
    </v-col>
  </v-row>
</template>

<script>
import { start } from "repl";
import { VDatePickerYears } from "vuetify/lib";
import axios from "axios";
import router from "../../router";
import { truncate } from "fs";
import { strict } from 'assert';
import { stringify } from 'querystring';

export default {
  data: () => ({
    newSchedule: {
      startTime: "00:00",
      endTime: "23:59",
      dates: [],
      useTime: false,
      name: "",
      details: "",
      color: ""
    },
    colors: [
      { text: "노랑", value: "amber" },
      { text: "파랑", value: "blue" },
      { text: "초록", value: "green" },
      { text: "빨강", value: "red" },
      { text: "보라", value: "purple" }
    ],
    scheduleType: "",
    landscape: true,
    private: true,
    dialog: false,
    focus: "",
    type: "month",
    typeToLabel: {
      month: "Month",
      week: "Week",
      day: "Day",
      "4day": "4 Days"
    },
    selectedEvent: {},
    selectedElement: null,
    selectedOpen: false,
    events: [],
    checkManager: null,
    userId: null,
    groupId: 0,
    managerId: -1
  }),
  mounted() {
    this.$refs.calendar.checkChange();
    this.checkPrivateSchdule();
    this.getUserId();
    if (!this.private) {
      this.getGroupIdManagerId();
    }
    this.$vuetify.goTo(0, {
      duration: 100,
      offset: 0
    })
  },
  computed: {
    dateRangeText() {
      this.sortDate();
      return this.newSchedule.dates.join(" ~ ");
    }, 
    scheduleTypeResult() {
      this.checkScheduleType()
      return this.scheduleType
    }
  },
  filters : {
    dateToString(val, status, isStart){
      const date = new Date(val);
      let str = ""
      if ((isStart && !status) || (status && date.getHours() < 9)){
        date.setDate(date.getDate() + 1)
        str = date.getUTCFullYear() + "년 "+(date.getUTCMonth() + 1)+"월 "+ date.getUTCDate() + "일 ";
      } else {
        str = date.getUTCFullYear() + "년 "+(date.getUTCMonth() + 1)+"월 "+ date.getUTCDate() + "일 ";
      }
      if(status){
        if (date.getHours() < 10) {
          str += " 0" + date.getHours()
        } else {
          str += " "+date.getHours()
        }
        if (date.getMinutes() < 10) {
          str += " : 0" + date.getMinutes()
        } else {

          str += " : " + date.getMinutes();
        }
      }
      

      return str;
    }
  },
  methods: {
     checkScheduleType() {
      if (this.selectedEvent.type==="U") {
        this.scheduleType = "개인일정"
      } else if (this.selectedEvent.cown===undefined){
        return
      } else {
        axios.get(`/study/user/${this.selectedEvent.cown}`)
          .then(res => {
            this.scheduleType = `"${JSON.parse(JSON.parse(res.data.object).group[0]).gpNm}" 그룹의 일정`
          })
          .catch(err => console.log(err))

      }
      
    },
    reset() {
      this.newSchedule = {
        startTime: "00:00",
        endTime: "23:59",
        dates: [],
        useTime: false,
        name: "",
        details: ""
      };
    },
    checkPrivateSchdule() {
      switch (this.$router.history.current.path) {
        case "/user/myschedule":
          this.private = true;
          break;
        default:
          this.private = false;
      }
    },
    getUserId() {
      axios
        .get("/user")
        .then(res => {
          this.userId = res.data.object.userId;
        })
        .catch(err => console.log(err));
    },
    getGroupIdManagerId() {
      const apiUrl = `/study/user/${this.$route.params.id}`;
      axios
        .get(apiUrl)
        .then(res => {
          const obj = JSON.parse(res.data.object);
          const data = JSON.parse(obj.group[0]);
          this.groupId = data.gpNo;
          this.managerId = data.gpMgrId;
        })
        .catch(err => {
          console.log(err);
        });
    },
    newForm() {
      if (this.private) {
        this.dialog = true;
      } else {
        if (this.userId === this.managerId) {
          this.dialog = true;
        } else {
          alert("그룹장만 그룹일정을 수정할 수 있습니다.");
        }
      }
    },
    viewDay({ date }) {
      this.focus = date;
      this.type = "day";
    },
    getEventColor(event) {
      return event.color;
    },
    setToday() {
      this.focus = "";
    },
    prev() {
      this.$refs.calendar.prev();
    },
    next() {
      this.$refs.calendar.next();
    },
    showEvent({ nativeEvent, event }) {
      const open = () => {
        this.selectedEvent = event;
        this.selectedElement = nativeEvent.target;
        setTimeout(() => (this.selectedOpen = true), 10);
      };

      if (this.selectedOpen) {
        this.selectedOpen = false;
        setTimeout(open, 10);
      } else {
        open();
      }

      nativeEvent.stopPropagation();
    },
    deleteSchedule(pk, type) {
      const event = [];
      if (type == "U") {
        axios
          .delete(`/cal/${pk}`)
          .then(res => {
            for (const e of this.events) {
              if (!(e.pk === pk)) {
                event.push(e);
              }
            }
            this.events = event;
          })
          .catch(err => {
            console.log(err);
          });
      }
      if (type == "G") {
        if (this.managerId === this.userId) {
          axios
            .delete(`/cal/${pk}`)
            .then(res => {
              for (const e of this.events) {
                if (!(e.pk === pk)) {
                  event.push(e);
                }
              }
              this.events = event;
            })
            .catch(err => {
              console.log(err);
            });
        } else {
          alert("스터디일정은 스터디페이지에서 그룹장만 삭제 가능합니다.")
        }
      }
      this.selectedOpen = false;
    },
    updateRange({ start, end }) {
      const event = [];
      const gpId = this.$route.params.id;
      if (this.$router.history.current.path === "/user/myschedule") {
        axios
          .get(`/cal/personal/${start.year}/${start.month}`)
          .then(res => {
            if (!(res.data.object === undefined)) {
              for (const schedule of res.data.object) {
                const s = schedule.cstTm;
                const e = schedule.cendTm;
                const addedSchdule = {
                  pk: schedule.cno,
                  name: schedule.cevtNm,
                  details: schedule.cevtDsc,
                  start: new Date(
                    s.slice(0, 4),
                    s.slice(5, 7) - 1,
                    s.slice(8, 10),
                    s.slice(11, 13),
                    s.slice(14, 16)
                  ),
                  end: new Date(
                    e.slice(0, 4),
                    e.slice(5, 7) - 1,
                    e.slice(8, 10),
                    e.slice(11, 13),
                    e.slice(14, 16)
                  ),
                  timed: schedule.useTime,
                  color: schedule.ccolor,
                  type: schedule.ctype,
                  cown: schedule.cown
                };
                event.push(addedSchdule);
              }
            }
          })
          .catch(err => console.log(err));
        axios
          .get(`/cal/group/${start.year}/${start.month}`)
          .then(res => {
            if (!(res.data.object === [])) {
              for (const schedule of res.data.object) {
                const s = schedule.cstTm;
                const e = schedule.cendTm;
                const addedSchdule = {
                  pk: schedule.cno,
                  name: schedule.cevtNm,
                  details: schedule.cevtDsc,
                  start: new Date(
                    s.slice(0, 4),
                    s.slice(5, 7) - 1,
                    s.slice(8, 10),
                    s.slice(11, 13),
                    s.slice(14, 16)
                  ),
                  end: new Date(
                    e.slice(0, 4),
                    e.slice(5, 7) - 1,
                    e.slice(8, 10),
                    e.slice(11, 13),
                    e.slice(14, 16)
                  ),
                  timed: schedule.useTime,
                  color: schedule.ccolor,
                  type: schedule.ctype,
                  cown: schedule.cown
                };
                event.push(addedSchdule);
              }
            }
          })
          .catch(err => console.log(err));
        this.events = event;
      } else {
        axios
          .get(`/cal/group/${gpId}/${start.year}/${start.month}`)
          .then(res => {
            if (!(res.data.object === undefined)) {
              for (const schedule of res.data.object) {
                const s = schedule.cstTm;
                const e = schedule.cendTm;
                const addedSchdule = {
                  pk: schedule.cno,
                  name: schedule.cevtNm,
                  details: schedule.cevtDsc,
                  start: new Date(
                    s.slice(0, 4),
                    s.slice(5, 7) - 1,
                    s.slice(8, 10),
                    s.slice(11, 13),
                    s.slice(14, 16)
                  ),
                  end: new Date(
                    e.slice(0, 4),
                    e.slice(5, 7) - 1,
                    e.slice(8, 10),
                    e.slice(11, 13),
                    e.slice(14, 16)
                  ),
                  timed: schedule.useTime,
                  color: schedule.ccolor,
                  type: schedule.ctype,
                  cown: schedule.cown
                };
                event.push(addedSchdule);
              }
            }
          })
          .catch(err => console.log(err));
        this.events = event;
      }
    },
    createNewSchedule() {
      if (this.newSchedule.dates.length !== 2) {
        alert("종료날짜를 입력해 주세요.");
        return;
      } else {
        if (this.newSchedule.useTime) {
          if (this.newSchedule.dates[0] === this.newSchedule.dates[1]) {
            if (this.newSchedule.startTime >= this.newSchedule.endTime) {
              alert("시간을 바르게 입력해 주세요.");
              return;
            }
          }
        }
      }
      if (this.newSchedule.color === ""){
        alert("색깔을 선택해 주세요")
        return
      }
      if (this.newSchedule.name === "") {
        alert("스케줄 내용을 입력해 주세요.");
        return;
      }
      const schedule = {
        cstTm: `${this.newSchedule.dates[0]}T${this.newSchedule.startTime}:00`,
        cendTm: `${this.newSchedule.dates[1]}T${this.newSchedule.endTime}:00`,
        useTime: this.newSchedule.useTime,
        cevtNm: this.newSchedule.name,
        cevtDsc: this.newSchedule.details,
        cown: this.private ? this.userId : this.groupId,
        ctype: this.private ? "U" : "G",
        ccolor: this.newSchedule.color
      };
      const apiUrl = "/cal/";
      axios
        .post(apiUrl, schedule)
        .then(res => {
          const schedule = res.data.object;
          const s = schedule.cstTm;
          const e = schedule.cendTm;
          const addedSchdule = {
            pk: schedule.cno,
            name: schedule.cevtNm,
            details: schedule.cevtDsc,
            start: new Date(
              s.slice(0, 4),
              s.slice(5, 7) - 1,
              s.slice(8, 10),
              s.slice(11, 13),
              s.slice(14, 16)
            ),
            end: new Date(
              e.slice(0, 4),
              e.slice(5, 7) - 1,
              e.slice(8, 10),
              e.slice(11, 13),
              e.slice(14, 16)
            ),
            timed: schedule.useTime,
            color: schedule.ccolor,
            type: schedule.ctype,
            cown: schedule.cown
          };
          this.events.push(addedSchdule);
        })
        .catch(err => console.log(err));

      this.dialog = false;
      this.reset();
    },
    sortDate() {
      this.newSchedule.dates.sort();
    }
  }
};
</script>

<style scoped>
  #radioGroup {
    padding-bottom: 10px;
  }

  #box {
    -ms-overflow-style: none; /* IE and Edge */
    scrollbar-width: none; /* Firefox */
  }
  #box::-webkit-scrollbar {
      display: none; /* Chrome, Safari, Opera*/
  }
</style>