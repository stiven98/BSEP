<template>
    <div class="q-gutter-y-md bg-primary" >
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
          <q-tab name="path" label="Certificate path" />
        </q-tabs>

        <q-tab-panels v-model="tab" animated class="bg-primary text-white" style="min-width:300px;min-height:400px ">
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
           <q-tree
      :nodes="chain"
      node-key="label"
      default-expand-all
      dark
    />
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
      tab: 'details',
      chain: []
    }
  },
  beforeMount () {
    this.createChain()
  },
  methods: {
    formatDate (date) {
      const dateTmp = date.toString()
      return dateTmp.split('T')[0]
    },
    createChain () {
      var list = []
      this.selectedCard.chain.forEach(el => {
        var obj = {
          label: el,
          children: []
        }
        list.push(obj)
      })
      var reversed = list.reverse()
      for (var i = 0; i < reversed.length - 1; i++) {
        reversed[i + 1].children.push(reversed[i])
      }
      this.chain.push(reversed[reversed.length - 1])
    }
  }
}
</script>
