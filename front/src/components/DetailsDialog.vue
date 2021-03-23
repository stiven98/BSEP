<template>
    <div class="q-gutter-y-md" style="max-width: 600px">
      <q-card>
        <q-tabs
          v-model="tab"
          dense
          class="bg-grey-3 text-grey-7"
          active-color="primary"
          indicator-color="purple"
          align="justify"
        >
          <q-tab name="details" label="Details" />
          <q-tab name="path" label="Certification path" />
        </q-tabs>

        <q-tab-panels v-model="tab" animated class="bg-primary text-white">
          <q-tab-panel name="details" v-if="selectedCard">
              Issued by: {{selectedCard.issuer}}
              <div>
            Issued to: {{selectedCard.subject}}
              </div>
            <div>
            Valid from: {{formatDate(selectedCard.startDate)}}
            </div>
            <div>
            Valid to: {{formatDate(selectedCard.endDate)}}
            </div>
            <div>
            Organization: {{selectedCard.organization}}
            </div>
             <div>
            Organization unit: {{selectedCard.organizationUnit}}
            </div>
            <div>
            Country: {{selectedCard.country}}
            </div>
             <div>
            Email: {{selectedCard.email}}
            </div>
            <div>
            Type: {{selectedCard.certificateType}}
            </div>
             <div>
            Issuer type: {{selectedCard.issuerType}}
            </div>

          </q-tab-panel>

          <q-tab-panel name="path">
            <div class="text-h6">Path</div>
            Lorem ipsum dolor sit amet consectetur adipisicing elit.
          </q-tab-panel>
        </q-tab-panels>
      </q-card>
         </div>
</template>
<script>
export default {
  props: ['selectedCard'],
  data () {
    return {
      tab: 'details'
    }
  },
  methods: {
    formatDate (date) {
      return date.split('T')[0]
    }
  }
}
</script>
