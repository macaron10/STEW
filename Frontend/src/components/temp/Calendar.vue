<template>
  <v-row class="fill-height">
    <v-col>
      <v-sheet height="64">
        <v-toolbar flat color="white">
          <v-btn outlined class="mr-4" color="grey darken-2" @click="setToday">Today</v-btn>
          <v-btn fab text small color="grey darken-2" @click="prev">
            <v-icon small>mdi-chevron-left</v-icon>
          </v-btn>
          <v-btn fab text small color="grey darken-2" @click="next">
            <v-icon small>mdi-chevron-right</v-icon>
          </v-btn>
          <v-toolbar-title v-if="$refs.calendar">{{ $refs.calendar.title }}</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-btn outlined color="grey darken-2" @click.stop="dialog = true">NEW</v-btn>
          <v-dialog v-model="dialog" max-width="480">
            <v-card>
              <v-card-title class="headline">New Schedule</v-card-title>
              <v-date-picker color='blue lighten-2' v-model="newSchedule.dates" :landscape="landscape" range></v-date-picker>
              <span v-if="newSchedule.dates[0]">기간 : {{dateRangeText}}</span>
              <v-switch v-model="newSchedule.useTime" class="ma-4" label="시간 사용"></v-switch>
              <v-text-field
                v-if="newSchedule.useTime"
                label="시작 시간"
                v-model="newSchedule.startTime"
                type="time"
              ></v-text-field>
              <v-text-field
                v-if="newSchedule.useTime"
                label="종료 시간"
                v-model="newSchedule.endTime"
                type="time"
              ></v-text-field>
              <v-text-field label="내용" v-model="newSchedule.name"></v-text-field>
              <v-text-field label="상세내용" v-model="newSchedule.details"></v-text-field>

              <v-btn color="primary" @click="createNewSchedule">등록</v-btn>
              <v-btn color="primary" @click="reset">초기화</v-btn>
            </v-card>
          </v-dialog>
          <v-spacer></v-spacer>
          <v-menu bottom right>
            <template v-slot:activator="{ on, attrs }">
              <v-btn outlined color="grey darken-2" v-bind="attrs" v-on="on">
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
              <v-list-item @click="type = '4day'">
                <v-list-item-title>4 days</v-list-item-title>
              </v-list-item>
            </v-list>
          </v-menu>
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
              <v-btn icon>
                <v-icon>mdi-pencil</v-icon>
              </v-btn>

              <v-btn icon>
                <v-icon>mdi-dots-vertical</v-icon>
              </v-btn>
            </v-toolbar>
            <v-card-text>
              <span>{{selectedEvent.start}}부터 {{selectedEvent.end}}까지</span>
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
export default {
  data: () => ({
    newSchedule: {
      startTime: "00:00",
      endTime: "23:59",
      dates: [],
      useTime: false,
      name: "",
      details: ""
    },
    landscape: true,

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
    events: [
      {
        name: "카카오 코딩 테스트 준비",
        details: "안녕",
        // start: "2020-07-23 15:00",
        start: new Date(2020, 6, 5, 15),
        // end: "2020-09-23 11:30",
        end: new Date(2020, 6, 11, 19),
        timed: false,
        color: "yellow"
      },
      {
        name: "네이버 코딩 테스트 준비",
        details: "안녕",
        // start: "2020-07-23 15:00",
        start: new Date(2020, 6, 16, 15),
        // end: "2020-09-23 11:30",
        end: new Date(2020, 6, 21, 19),
        timed: false,
        color: "green"
      },
      {
        name: "Toss 코딩 테스트 준비",
        details: "안녕",
        // start: "2020-07-23 15:00",
        start: new Date(2020, 6, 23, 15),
        // end: "2020-09-23 11:30",
        end: new Date(2020, 6, 31, 19),
        timed: false,
        color: "blue"
      },
      {
        name: "모의 코딩 테스트",
        details: "안녕",
        // start: "2020-07-23 15:00",
        start: new Date(2020, 6, 25, 15),
        // end: "2020-09-23 11:30",
        end: new Date(2020, 6, 25, 19),
        timed: false,
        color: "orange"
      },
      {
        name: "모의 코테 분석",
        details: "안녕",
        // start: "2020-07-23 15:00",
        start: new Date(2020, 6, 26, 15),
        // end: "2020-09-23 11:30",
        end: new Date(2020, 6, 26, 19),
        timed: false,
        color: "indigo"
      },
    ],
    colors: [
      "blue",
      "indigo",
      "deep-purple",
      "cyan",
      "green",
      "orange",
      "grey darken-1"
    ],
    names: [
      "Meeting",
      "Holiday",
      "PTO",
      "Travel",
      "Event",
      "Birthday",
      "Conference",
      "Party"
    ]
  }),
  mounted() {
    this.$refs.calendar.checkChange();
  },
  computed: {
    dateRangeText() {
      this.sortDate();
      return this.newSchedule.dates.join(" ~ ");
    }
  },
  methods: {
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
    updateRange({ start, end }) {
      console.log();
      // 일정 가져오는 axios보내야 하는 함수
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
      if (this.newSchedule.name===""){
        alert("스케줄 내용을 입력해 주세요.")
        return
      }
      const schedule = {
        cstTm: `${this.newSchedule.dates[0]}T${this.newSchedule.startTime}:00`,
        cendTm: `${this.newSchedule.dates[1]}T${this.newSchedule.endTime}:00`,
        useTime: this.newSchedule.useTime,
        cevtNm: this.newSchedule.name,
        cevtDsc: this.newSchedule.details,
        cown: 1, //id number
        ctype: 'G'  //or 'U' 
      };

      // 등록 axios
      this.dialog = false
      this.updateRange()
      this.reset()
    },
    sortDate() {
      this.newSchedule.dates.sort();
    }
  }
};
</script>