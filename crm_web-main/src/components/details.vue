<template>
  <div class="enter">
    <div v-if="enter">
      <van-nav-bar
        title="商户详情"
        left-text=""
        left-arrow
        @click-left="onClickLeft"
      />
      <!-- 1 -->
      <div class="divborder">
        <van-field
          v-show="edit == false"
          clickable
          readonly
          :label-width="labelWidth"
          name="one"
          input-align="right"
          :value="dataList.industryType"
          label="所在行业"
          style="border-bottom: 10px solid #ebedf0"
        >
          <template #input>
            <span v-if="hangye.one">{{ hangye.one }}</span>
            <span v-if="hangye.two">-{{ hangye.two }}</span>
            <span v-if="hangye.three">-{{ hangye.three }}</span>
          </template>
        </van-field>
        <van-field
          v-show="edit"
          clickable
          is-link
          readonly
          :label-width="labelWidth"
          name="one"
          input-align="right"
          :value="dataList.industryType"
          label="所在行业"
          required
          placeholder="请选择"
          style="border-bottom: 10px solid #ebedf0"
        >
          <template #input>
            <van-row style="width: 100%">
              <van-col span="8" @click="showOne = true">{{
                hangye.one
              }}</van-col>
              <van-popup v-model="showOne" position="bottom">
                <van-picker
                  value-key="categoryName"
                  show-toolbar
                  :columns="oneLevelhangye"
                  @confirm="oneLevel"
                  @cancel="showOne = false"
                />
              </van-popup>
              <van-col span="8" @click="showTwoList" :disabled="hangyetwoBtn">{{
                hangye.two
              }}</van-col>
              <van-popup v-model="showTwo" position="bottom">
                <van-picker
                  value-key="categoryName"
                  show-toolbar
                  columns-num
                  :columns="twoLevelhangye"
                  @confirm="twoLevel"
                  @cancel="showTwo = false"
                />
              </van-popup>
              <van-col
                span="8"
                @click="showThreeList"
                :disabled="hangyeThreeBtn"
                >{{ hangye.three }}</van-col
              >
              <van-popup v-model="showThree" position="bottom">
                <van-picker
                  value-key="categoryName"
                  show-toolbar
                  columns-num
                  :columns="threeLevelhangye"
                  @confirm="threeLevel"
                  @cancel="showThree = false"
                />
              </van-popup>
            </van-row>
          </template>
        </van-field>
        <div class="topflex">
          <div>证件信息</div>
        </div>
        <div style="display: flex; align-item: center">
          <van-field
            name="uploader"
            error
            label="营业执照"
            class="upload"
            :required="edit"
          >
            <template #input>
              <van-uploader
                v-model="licenseImg"
                :max-count="1"
                :after-read="afterRead"
                class="uploadImg"
                name="1"
                :before-delete="delImg"
                style="display: inline-block"
                :readonly="edit == false"
                :deletable="edit"
              />
            </template>
          </van-field>
          <van-field
            name="uploader"
            error
            label="店招照片"
            class="upload"
            :required="edit && afterTime"
          >
            <template #input>
              <van-uploader
                v-model="shopHeadPhotoUrl"
                :max-count="1"
                :after-read="afterRead"
                class="uploadImg"
                name="2"
                style="display: inline-block"
                :readonly="edit == false"
                :deletable="edit"
                :before-delete="delImg"
              />
            </template>
          </van-field>
          <van-field
            v-if="showLicenseKey || edit == true"
            name="uploader"
            error
            label="许可证"
            class="upload"
            :required="xuke && edit == true"
          >
            <template #input>
              <van-uploader
                v-model="licenseKey"
                :max-count="1"
                :after-read="afterRead"
                class="uploadImg"
                style="display: inline-block"
                :deletable="edit"
                :before-delete="delImg"
                name="3"
                :readonly="edit == false"
              />
            </template>
          </van-field>
          <van-field
            v-if="
              licenseKey.length == 0 &&
              shopHeadPhotoUrl.length > 0 &&
              edit == false
            "
            disabled
          ></van-field>
        </div>
        <div style="display: flex; align-item: center">
          <van-field
            v-if="fileImageUrl.length > 0 || edit == true"
            name="uploader"
            error
            label="备案证"
            class="upload"
            :required="beian && edit == true"
          >
            <template #input>
              <van-uploader
                v-model="fileImageUrl"
                :max-count="1"
                :after-read="afterRead"
                :before-delete="delImg"
                class="uploadImg"
                name="4"
                style="display: inline-block"
                :deletable="edit"
                :readonly="edit == false"
              />
            </template>
          </van-field>
          <!-- <van-field v-if="fileImageUrl.length>0 || edit == true"/> -->
          <van-field
            v-if="hygieneImageUrl.length > 0 || edit == true"
            name="uploader"
            error
            label="卫生许可证"
            class="upload"
          >
            <template #input>
              <van-uploader
                v-model="hygieneImageUrl"
                :max-count="1"
                :after-read="afterRead"
                class="uploadImg"
                :before-delete="delImg"
                name="5"
                style="display: inline-block"
                :deletable="edit"
                :readonly="edit == false"
              />
            </template>
          </van-field>
          <van-field
            v-if="
              hygieneImageUrl.length > 0 ||
              fileImageUrl.length > 0 ||
              edit == true
            "
            disabled
          ></van-field>
        </div>
        <van-field
          v-model="dataList.businessLicense.storeName"
          :readonly="edit == false"
          :required="edit"
          :label-width="labelWidth"
          name="营业执照名称"
          label="营业执照名称"
          show-error
          maxlength="127"
          input-align="right"
          style="border-top: 1px solid transparent"
          placeholder="请输入"
        />
        <van-field
          v-model="dataList.businessLicense.representName"
          :readonly="edit == false"
          :required="edit"
          name="法人代表人/经营者"
          :label-width="180"
          label="法人代表人/经营者"
          show-error
          input-align="right"
          placeholder="请输入"
        />
        <van-field
          v-model="dataList.businessLicense.socialCreditCode"
          :readonly="edit == false"
          :required="edit"
          name="信用代码"
          show-error
          input-align="right"
          :label-width="labelWidth"
          label="信用代码"
          placeholder="请输入"
        />
        <van-field
          v-model="dataList.businessLicense.businessType"
          readonly
          name="经营类型"
          type="textarea"
          rows="1"
          autosize
          show-error
          input-align="right"
          :label-width="60"
          label="经营类型"
          placeholder="请输入"
        />
        <!-- 地区 -->
        <van-field
          v-model="dataList.businessLicense.detailedAddress"
          :readonly="edit == false"
          :required="edit"
          name="地址"
          show-error
          label="地址"
          type="textarea"
          rows="1"
          autosize
          :label-width="40"
          input-align="right"
          placeholder="请输入"
        />
        <van-field
          v-show="edit == false"
          readonly
          name="validPermanent"
          input-align="right"
          label="营业执照有效期"
          show-error
          placeholder="请输入"
          :label-width="labelWidth"
        >
          <template #input>
            <span v-if="validPermanent == '1'">{{ validPeriod }}</span>
            <span v-if="validPermanent == '2'">永久有效</span>
          </template>
        </van-field>
        <van-field
          v-show="edit"
          required
          name="validPermanent"
          input-align="right"
          label="营业执照有效期"
          show-error
          placeholder="请选择"
          :label-width="labelWidth"
        >
          <template #input>
            <van-radio-group v-model="validPermanent" direction="horizontal">
              <van-radio name="1">指定时间</van-radio>
              <van-radio name="2">永久有效</van-radio>
            </van-radio-group>
          </template>
        </van-field>
        <van-field
          v-if="validPermanent == '1' && edit == true"
          is-link
          readonly
          required
          input-align="right"
          :label-width="labelWidth"
          name="calendar"
          :value="validPeriod"
          label="证件有效截止日期"
          placeholder="请选择截止日期"
          @click="
            showvalidDate = true;
            number = 3;
          "
        />
       <van-popup v-model="showvalidDate" position="bottom">
                  <van-datetime-picker
                    type="date"
                    title="选择年月日"
                    v-model="defaultDate"
                    :max-date="maxDate"
                    @confirm="getSelectDate"
                    :formatter="formatterDate"
                    @cancel="showvalidDate = false"
                  />
                </van-popup>
        <van-field
          v-show="edit == false && licenseKey.length > 0"
          readonly
          name="validPermanent"
          input-align="right"
          label="许可证有效期"
          show-error
          :value="dataList.foodBusinessLicense.validityTerm"
          :label-width="labelWidth"
        >
        </van-field>
        <van-field
          v-show="licenseKey !== '' && licenseKey.length > 0 && edit == true"
          is-link
          readonly
          required
          input-align="right"
          :label-width="140"
          name="calendar"
          ref="socialCreditCode"
          :value="dataList.foodBusinessLicense.validityTerm"
          label="许可证有效期"
          placeholder="请选择"
          @click="
            showvalidDate = true;
            number = 1;
          "
        />
        
        <van-field
          v-show="edit == false && fileImageUrl.length > 0"
          readonly
          name="validPermanent"
          input-align="right"
          label="备案证有效期"
          show-error
          :value="dataList.recordValidTerm"
          :label-width="labelWidth"
        >
        </van-field>
        <van-field
          v-show="
            fileImageUrl !== '' && fileImageUrl.length > 0 && edit == true
          "
          is-link
          readonly
          required
          input-align="right"
          :label-width="140"
          name="calendar"
          ref="socialCreditCode"
          :value="dataList.recordValidTerm"
          label="备案证有效期"
          placeholder="请选择"
          @click="
            showvalidDate = true;
            number = 2;
          "
        />
       
      </div>
      <!-- 2 -->
      <div class="divborder">
        <div class="topflex">
          <div>经营信息</div>
          <div v-show="showRight">
            <div v-if="showJy" class="speard" @click="showJy = false">
              收起<van-icon name="arrow-up" class="icon" />
            </div>
            <div v-else class="speard" @click="showJy = true">
              展开<van-icon name="arrow-down" class="icon" />
            </div>
          </div>
        </div>
        <van-field
          :readonly="edit == false"
          :required="edit"
          v-model="dataList.storeName"
          name="店招名称"
          :label-width="labelWidth"
          label="店招名称"
          input-align="right"
        />
        <van-field
          v-show="edit == false"
          input-align="right"
          label="是否正常营业"
          label-width="180"
        >
          <template #input>
            <span v-if="operatingState">正常营业</span>
            <span v-else>暂停营业</span>
          </template>
        </van-field>
        <van-field
          v-show="edit"
          required
          name="switch"
          input-align="right"
          label="是否正常营业"
          label-width="180"
        >
          <template #input>
            <van-switch v-model="operatingState" size="20" disabled />
          </template>
        </van-field>

        <van-field
          :readonly="edit == false"
          :required="edit"
          v-model="dataList.operatorName"
          name="实际经营人姓名"
          :label-width="labelWidth"
          label="实际经营人姓名"
          input-align="right"
        />
        <van-field
          readonly
          v-model="dataList.operatorPhone"
          type="tel"
          :error="error1"
          maxlength="11"
          :required="edit"
          :label-width="labelWidth"
          label="电话号码"
          input-align="right"
          :formatter="formatter"
          @click="phone = '1'"
        />
        <div v-if="showJy">
          <van-field
            v-show="edit == false"
            readonly
            input-align="right"
            label="是否开通支付"
            :label-width="labelWidth"
          >
            <template #input>
              <span v-if="isOpen == 'true'">是</span>
              <span v-else>否</span>
            </template>
          </van-field>
          <van-field
            v-show="edit"
            required
            name="switch"
            input-align="right"
            label="是否开通支付"
            :label-width="labelWidth"
          >
            <template #input>
              <van-radio-group
                v-model="isOpen"
                :disabled="showApptime"
                direction="horizontal"
                @change="isOpenpay"
              >
                <van-radio name="true">是</van-radio>
                <van-radio name="false">否</van-radio>
              </van-radio-group>
            </template>
          </van-field>
          <!-- 预约银行 -->
          <div>
            <!-- 不可编辑 -->
            <van-field
              v-show="edit == false && isOpen == 'true'"
              readonly
              clickable
              input-align="right"
              :label-width="labelWidth"
              name="bank"
              :value="dataList.bankName"
              label="预约银行"
            />
            <van-field
              v-show="edit == true && isOpen == 'true'"
              readonly
              required
              is-link
              clickable
              input-align="right"
              :label-width="labelWidth"
              name="bank"
              :value="dataList.bankName"
              label="预约银行"
              placeholder="请选择银行"
              @click="showBank"
            />
            <van-popup v-model="showBankList" position="bottom">
              <van-picker
                value-key="bankName"
                show-toolbar
                :columns="bankList"
                @confirm="getBank"
                @cancel="showBankList = false"
              />
            </van-popup>
          </div>
          <!-- 预约开通日期 -->
          <div>
            <!-- 不可编辑 -->
            <van-field
              v-show="edit == false && isOpen == 'true'"
              readonly
              input-align="right"
              :label-width="labelWidth"
              name="calendar"
              :value="appointmentBank.appointmentTime"
              label="预约开通日期"
            />
            <van-field
              v-show="edit == true && isOpen == 'true'"
              is-link
              readonly
              required
              v-if="isOpen"
              input-align="right"
              :label-width="labelWidth"
              name="calendar"
              :value="appointmentBank.appointmentTime"
              label="预约开通日期"
              placeholder="请选择"
              @click="showDateSelect"
            />
            <van-calendar
              v-model="showDate"
              color="#1989fa"
              @confirm="onDate"
            />
          </div>

          <!-- 实际经营面积 -->
          <div>
            <!-- 不可编辑 -->
            <van-field
              v-show="edit == false"
              readonly
              input-align="right"
              :label-width="140"
              name="area"
              :value="storeArea_Name"
              label="实际经营面积"
            />
            <van-field
              v-show="edit"
              required
              clickable
              readonly
              is-link
              input-align="right"
              :label-width="140"
              name="area"
              :value="storeArea_Name"
              label="实际经营面积"
              placeholder="请选择"
              @click="showStoreArea = true"
            />
            <van-popup v-model="showStoreArea" position="bottom">
              <van-picker
                value-key="area"
                show-toolbar
                :columns="areaOption"
                @confirm="getAreaOption"
                @cancel="showStoreArea = false"
              />
            </van-popup>
          </div>
          <van-field
            v-show="edit == false"
            input-align="right"
            label="是否在学校周边50米"
            label-width="180"
          >
            <template #input>
              <span v-if="dataList.awayFromSchool == 'true'">是</span>
              <span v-else>否</span>
            </template>
          </van-field>
          <van-field
            v-show="edit"
            required
            name="switch"
            input-align="right"
            label="是否在学校周边50米"
            label-width="180"
          >
            <template #input>
              <van-radio-group
                v-model="dataList.awayFromSchool"
                direction="horizontal"
              >
                <van-radio name="true">是</van-radio>
                <van-radio name="false">否</van-radio>
              </van-radio-group>
              <!-- <van-switch v-model="dataList.awayFromSchool" size="20" /> -->
            </template>
          </van-field>
          <van-field
            v-show="edit == false"
            input-align="right"
            label="是否是奶茶店"
            label-width="180"
          >
            <template #input>
              <span v-if="dataList.isMilkyTea == 'true'">是</span>
              <span v-else>否</span>
            </template>
          </van-field>
          <van-field
            v-show="edit"
            required
            name="switch"
            input-align="right"
            label="是否是奶茶店"
            label-width="180"
          >
            <template #input>
              <van-radio-group
                v-model="dataList.isMilkyTea"
                direction="horizontal"
              >
                <van-radio name="true">是</van-radio>
                <van-radio name="false">否</van-radio>
              </van-radio-group>
              <!-- <van-switch v-model="dataList." size="20" /> -->
            </template>
          </van-field>
          <van-field
            v-show="edit == false"
            input-align="right"
            label="是否提供外卖"
            label-width="180"
          >
            <template #input>
              <span v-if="dataList.isTakeOut == 'true'">是</span>
              <span v-else>否</span>
            </template>
          </van-field>
          <van-field
            v-show="edit"
            required
            name="switch"
            input-align="right"
            label="是否提供外卖"
            label-width="120"
          >
            <template #input>
              <van-radio-group
                v-model="dataList.isTakeOut"
                direction="horizontal"
              >
                <van-radio name="true">是</van-radio>
                <van-radio name="false">否</van-radio>
              </van-radio-group>
              <!-- <van-switch v-model="dataList.isTakeOut" size="20" /> -->
            </template>
          </van-field>
        </div>
      </div>
      <!-- 3 -->
      <div class="divborder">
        <div class="topflex">
          <div>监管信息</div>
          <div v-show="showRight">
            <div v-if="showSuper" class="speard" @click="showSuper = false">
              收起<van-icon name="arrow-up" class="icon" />
            </div>
            <div v-else class="speard" @click="showSuper = true">
              展开<van-icon name="arrow-down" class="icon" />
            </div>
          </div>
        </div>
        <div v-show="showSuper" class="mapBox">
          <van-field
            v-show="edit == false"
            readonly
            v-model="dataList.address"
            name="实际地址"
            :label-width="60"
            label="实际地址"
            input-align="right"
            autosize
            rows="1"
            type="textarea"
          />
          <van-field
            v-show="edit"
            v-model="dataList.address"
            name="实际地址"
            required
            :label-width="60"
            label="实际地址"
            input-align="right"
            autosize
            rows="1"
            type="textarea"
            placeholder="请输入"
          />

          <van-field
            v-show="edit"
            v-model="address"
            name="定位"
            :label-width="60"
            label="定位"
            input-align="right"
            autosize
            rows="1"
            type="textarea"
            placeholder="请输入"
            right-icon="location-o"
            @click="selectPosition"
          />
          <van-field
            :required="edit"
            readonly
            :label-width="labelWidth"
            :value="dataList.addressCode"
            label="省市区"
            name="city"
            input-align="right"
          >
            <template #input>
              <span v-if="youdiqu"
                >{{ region.province }}{{ region.city }}{{ region.area }}</span
              >
            </template>
          </van-field>
          <!-- <van-field
            readonly
            is-link
            required
            v-show="edit"
            :label-width="labelWidth"
            :value="dataList.addressCode"
            label="省市区"
            name="city"
            input-align="right"
            placeholder="请选择"
          >
            <template #input>
              <van-row style="width: 100%">
                <van-col span="8" @click="showProvince = true">{{
                  region.province
                }}</van-col>
                <van-popup v-model="showProvince" position="bottom">
                  <van-picker
                    value-key="name"
                    show-toolbar
                    :columns="listProvince"
                    @confirm="onProvince"
                    @cancel="showProvince = false"
                  />
                </van-popup>
                <van-col span="8" @click="showCityList">{{
                  region.city
                }}</van-col>
                <van-popup v-model="showCity" position="bottom">
                  <van-picker
                    value-key="name"
                    show-toolbar
                    columns-num
                    :columns="listCity"
                    @confirm="onCity"
                    @cancel="showCity = false"
                  />
                </van-popup>
                <van-col span="8" @click="showAreaList">{{
                  region.area
                }}</van-col>
                <van-popup v-model="showArea" position="bottom">
                  <van-picker
                    value-key="name"
                    show-toolbar
                    :columns="listArea"
                    @confirm="onArea"
                    @cancel="showArea = false"
                  />
                </van-popup>
              </van-row>
            </template>
          </van-field> -->
          <!-- 监管所 -->
          <div>
            <van-field
              readonly
              :label-width="labelWidth"
              input-align="right"
              name="supervisionName"
              :value="dataList.supervisionName"
              label="监管所"
              :required="edit"
              ><template #input>
                <span v-if="dataList.supervisionName">{{
                  dataList.supervisionName
                }}</span>
              </template>
            </van-field>
            <!-- 
            <van-field
              v-show="edit"
              clickable
              readonly
              is-link
              required
              :label-width="labelWidth"
              input-align="right"
              name="supervisionName"
              :value="dataList.supervisionName"
              label="监管所"
              @click="selectSupervision"
              placeholder="请选择"
            />
            <van-popup v-model="showgov" position="bottom">
              <van-picker
                value-key="supervisionName"
                show-toolbar
                :columns="listGov"
                @confirm="onGov"
                @cancel="showgov = false"
              />
            </van-popup> -->
          </div>
          <!-- 社区 -->
          <div>
            <van-field
              v-show="edit == false"
              readonly
              :label-width="labelWidth"
              input-align="right"
              name="communityName"
              :value="dataList.communityName"
              label="所属社区"
              :required="edit"
              ><template #input>
                <span v-if="dataList.communityName">{{
                  dataList.communityName
                }}</span>
              </template>
            </van-field>
            <van-field
              v-show="edit"
              v-if="showShequ || dataList.communityName"
              clickable
              readonly
              is-link
              required
              :label-width="labelWidth"
              input-align="right"
              name="communityName"
              :value="dataList.communityName"
              label="所属社区"
              placeholder="请选择"
              @click="selectCommunit"
            />
            <van-popup v-model="showcommunit" position="bottom">
              <van-picker
                value-key="communityName"
                show-toolbar
                :columns="listCommunit"
                @confirm="onCommunit"
                @cancel="showcommunit = false"
              />
            </van-popup>
          </div>
        </div>
      </div>
      <div class="topflex bottomflex">
        <div>负责BD：{{ dataList.bd }}</div>
        <div>负责BDM：{{ dataList.bdm }}</div>
      </div>
      <div style="padding: 10px 16px 20px 16px">
        <van-button
          v-show="edit == false"
          round
          block
          type="info"
          native-type="submit"
          @click="isEdit"
          >编辑</van-button
        >
        <van-button
          v-show="edit"
          round
          block
          type="info"
          native-type="submit"
          @click="onCommit"
          >保存</van-button
        >
      </div>

      <!-- 二次确认 -->
      <van-dialog
        v-model="showDialog"
        title="确认提交"
        message="请确认填写资料是否有误，确认无误后可提交入驻"
        show-cancel-button
        :before-close="beforeClose"
      >
      </van-dialog>
      <!-- 许可证备案证  备注弹框 -->
      <van-dialog
        v-model="showNote"
        title="您并未上传许可证/备案证?"
        style="padding: 10px"
        :show-confirm-button="false"
        ><van-field name="checkboxGroup" required label="备注" label-width="40">
        </van-field>
        <van-field name="checkboxGroup">
          <template #input>
            <van-radio-group
              v-model="dataList.certificateRemark"
              direction="horizontal"
            >
              <van-radio name="1">商家未办理证件</van-radio>
              <van-radio name="2">商家不配合</van-radio>
              <van-radio name="3">其他</van-radio>
            </van-radio-group>
          </template>
        </van-field>
        <van-field
          v-model="dataList.certificateRemarkOther"
          placeholder="请输入备注内容"
          rows="5"
          autosize
          required
          border
          maxlength="200"
          type="textarea"
          show-word-limit
          style="border: 1px solid #ebedf0"
        />
        <div class="flexBtn">
          <van-button class="footerBtn" plain round @click="showNote = false"
            >取消</van-button
          >
          <van-button
            class="footerOk"
            type="info"
            round
            @click="onConfirm"
            :loading="noteLoading"
            >确定</van-button
          >
        </div>
      </van-dialog>
    </div>
    <div v-else>
      <baidu-map
        ref="child"
        :addressInfo="dataList.address"
        @buttonEvent="showMapaddress"
      ></baidu-map>
    </div>

    <!-- 内容 -->
  </div>
</template>
<script>
import baiduMap from "./baiduMap.vue";
import Exif from "exif-js";
export default {
  components: {
    baiduMap,
  },
  data() {
    return {
      createTime: "", //入驻日期
      afterTime: true, //是否在5.16号之前
      enter: true,
      licenseImg: [], //营业执照
      shopHeadPhotoUrl: [], //店招照片
      fileImageUrl: [], //备案证
      hygieneImageUrl: [], //卫生许可证
      licenseKey: [], //许可证
      showLicenseKey: false, //显示许可证
      dataImg: [], //营业执照识别信息
      listProvince: [], //省级弹框数据
      showCity: false, //市级弹框
      listCity: [], //市级弹框数据
      showArea: false, //区级弹框
      listArea: [], //区级弹框数据
      showProvince: false, //省市区下拉框
      showgov: false, //监管所
      showcommunit: false, //社区
      listGov: [], //监管所
      listCommunit: [], //所属社区列表
      industryList: [], //行业列表
      bankList: [], //银行列表
      showBankList: false, //银行弹框
      showDate: false, //时间弹框
      industryType_name: "", //行业名
      industryType: "", //行业
      oneLevelhangye: [], //一级行业
      twoLevelhangye: [], //二级行业
      threeLevelhangye: [], //二级行业
      showOne: false, //行业弹框
      showTwo: false, //行业弹框
      showThree: false, //行业弹框
      showDialog: false, //提示框
      showvalidDate: false, //显示指定时间日历弹框
      number: "", //显示日历弹框
      maxdate: new Date(2100, 0, 31), //永久
      showApptime: false, //是否可以更改银行信息  false-是   true--不可更改
      areaOption: [
        { id: 1, area: "150平方米以下" },
        { id: 2, area: "151-500平方米" },
        { id: 3, area: "501-1000平方米" },
        { id: 4, area: "1001-3000平方米" },
        { id: 5, area: "3001以上平方米" },
      ],
      storeArea_Name: "", //实际经营面积
      showStoreArea: false, //实际经营面积
      hangyetwoBtn: false, //限制行业选择生产
      hangyeThreeBtn: false, //限制行业选择生产
      appointmentBank: {
        appointmentTime: "", //预约开通日期
        bankId: "", //银行ID
      },
      operatingState: "", //是否正常营业   true:正常营业   false:暂停营业
      dataList: {
        certificateRemarkOther: "", //备注内容
        certificateRemark: "", //备注按钮
        lng: null,
        lat: null,
        address: "",
        addressCode: "",
        businessLicense: {},
        merchantId: "",
        operatorName: "",
        operatorPhone: "",
        status: "",
        storeArea: "",
        storeName: "",
        supervisionName: "",
        isMilkyTea: "", //是否是奶茶店
        isTakeOut: "", //是否提供外卖
        operatingState: "", //是否正常营业    //是否正常营业   false:正常营业   true:暂停营业
        awayFromSchool: "", //是否学校周边50米
        industryType: "", //行业
        communityName: "", //所属社区
        foodBusinessLicense: {}, //食品许可证
      },
      validPeriod: "", //有效期时间
      // validPermanentText: "", //有 效 期文字
      validPermanent: "", // 有 效 期id // validPermanent  true,1---永久有效  false,2-指定日期
      foodBusinessLicense: {}, //食品许可证
      isOpen: "", //是否开通支付
      files: {
        //压缩图片
        name: "",
        type: "",
      },
      isChecked: false, //表单是否填完
      labelWidth: "8em",
      diqu: [], //详情地区
      region: {
        city: "请选择市", //市级
        province: "请选择省", //省级
        area: "请选择区", //区
      },
      region_province_code: "",
      region_city_code: "",
      region_area_code: "",
      // hangyeArr: [], //行业
      hangye: {
        one: "请选择", //一级行业
        two: "请选择", //二级行业
        three: "请选择", //三级行业
      },
      youdiqu: false, //有地区
      youhangye: false, //有行业
      edit: false, //编辑按钮
      showJy: false, //展开经营信息按钮
      showSuper: false, //展开监管信息
      showOther: false, //展开其他信息
      showRight: true, //显示展开搜索文字
      error1: false, //电话内容是否变红  经营人
      error2: false, //电话内容是否变红   推荐人
      phone: "", //1 为经营人电话号码  2--推荐人
      industryName: "", //详情行业名字
      code: "", //判断是首页 还是商户列表
      scroll: "", //滚动位置
      showShequ: false, //是否显示社区 输入框
      address: "", //定位输入框内容
      beian: false,
      xuke: false, //
      showNote: false, //
      noteLoading: false, //备注按钮loading
      maxDate: new Date(2099, 10, 1),
      defaultDate:'',//当前时间
    };
  },
  created() {},

  mounted() {
    this.getDetails();
    this.getBankInfo(); //银行
    this.postPlace();
    this.getToday(); //获取当前时间
    // 行业类别
    setTimeout(() => {
      this.onIndustryCategory();
    }, 1000);
    //判断日期是否在5.16号之前  店招照片是否必填
    let time = this.$route.query.time;
    this.createTime = time.slice(0, 10);
    if (this.createTime < "2022-05-16") {
      this.afterTime = false;
    } else {
      this.afterTime = true;
    }
    console.log(this.afterTime, "time");
  },

  methods: {
    //获取地图信息
    showMapaddress(val) {
      this.enter = true;
      if (val !== "1") {
        this.address = val.address;
        this.dataList.lng = val.point.lng;
        this.dataList.lat = val.point.lat;
      }
    },
    // 获取省市区
    getDetailsArea() {
      //省
      let dataProvince = {
        parentCode: this.region_province_code,
        level: 2,
      };
      this.$api.areaList(dataProvince).then((res) => {
        let data = res.data.data;
        this.listCity = data; //市
      });
      //市
      let dataCity = {
        parentCode: this.region_city_code,
        level: 3,
      };
      this.$api.areaList(dataCity).then((res) => {
        let data = res.data.data;
        this.listArea = data; //区
      });
      //监管所
      let govId = {
        addressId: this.region_area_code,
      };
      // 获取监管所API
      this.$api.supervisionList(govId).then((res) => {
        // console.log(res.data.data, "尖端所");
        this.listGov = res.data.data;
      });
    },
    //行业
    //行业下拉
    getHangye() {
      console.log(this.hangye.two, "hangye2");
      if (
        this.hangye_one_categoryId !== "2" ||
        this.hangye_one_categoryId !== "54"
      ) {
        let data = {
          categoryId: this.hangye_one_categoryId,
          categoryName: this.hangye.one,
        };
        let data2 = {
          categoryId: this.hangye_two_categoryId,
        };
        this.$api.industryList(data).then((res) => {
          this.twoLevelhangye = res.data.data;
        });
        this.$api.industryList(data2).then((res) => {
          this.threeLevelhangye = res.data.data;
          if (this.threeLevelhangye.length <= 0) {
            this.hangye.three = "无";
            this.hangyeThreeBtn = true;
          }
        });
      }
    },
    // 获取详情信息
    getDetails() {
      // this.$router.go(0)
      const load = this.$toast.loading({
        duration: 0,
        forbidClick: true,
        message: "加载中...",
      });
      this.id = this.$route.query.id;
      let data = {
        id: this.id,
      };
      this.$api
        .mechDetails(data)
        .then((res) => {
          // console.log(res.data.status, "状态码");
          // console.log(res, "res");
          if (res.data.status == 200) {
            load.clear();
            let data = res.data.data;
            console.log(data, "data");
             if (data.supervisionId) {
              this.getCommunit(this.dataList.supervisionId); //获取社区
            }
            this.dataList.storeName = data.storeName;
            this.dataList.address = data.address;
            console.log(data.address, "030303");
            this.dataList.supervisionId = data.supervisionId;
           
            this.dataList.communityName = data.communityName;
            this.dataList.communityId = data.communityId;
            this.dataList.lng = data.lng;
            this.dataList.lat = data.lat;
            this.dataList.merchantId = data.merchantId;
            this.dataList.addressCode = data.addressCode;
            this.dataList.awayFromSchool = String(data.awayFromSchool);
            this.dataList.isMilkyTea = String(data.isMilkyTea); //是否是奶茶店
            this.dataList.isTakeOut = String(data.isTakeOut); //是否提供外卖

            // 陈康
            this.dataList.operatorName = data.operatorName;
            this.dataList.operatorPhone = data.operatorPhone;
            this.dataList.bankName = data.bankName;

            this.dataList.supervisionName = data.supervisionName;

            this.dataList.bd = data.bd;
            this.dataList.bdId = parseInt(data.bdId);
            this.dataList.bdm = data.bdm;
            
            // 陈康
            console.log(
              this.dataList.isTakeOut,
              data.isTakeOut,
              this.dataList.isMilkyTea == "true",
              "this.industryType"
            );
            this.operatingState = !data.operatingState;

            console.log(data.lng, data.lat, "lng1,lat1");
            if (data.industryType !== undefined) {
              this.industryType = data.industryType;
            }
            // 状态
            this.status = data.status;
            if (this.status == "3" || this.status == "4") {
              this.showApptime = true;
            } else {
              this.showApptime = false;
            }
            //  店招照片
            if (data.shopHeadPhotoUrl) {
              this.shopHeadPhotoUrl = [{ url: data.shopHeadPhotoUrl }];
            } else {
              this.shopHeadPhotoUrl = [];
            }

            //  卫生许可证

            if (data.hygieneImageUrl) {
              this.hygieneImageUrl = [{ url: data.hygieneImageUrl }];
            } else {
              this.hygieneImageUrl = [];
            }

            //  备案证
            if (data.fileImageUrl) {
              this.fileImageUrl = [{ url: data.fileImageUrl }];
            } else {
              this.fileImageUrl = [];
            }
            //营业执照信息
            console.log(data.businessLicense == true, "营业执照信息1");
            if (data.businessLicense) {
              let business = data.businessLicense;
              this.dataList.businessLicense = business;
              this.licenseImg = [{ url: business.imagesUrl }];
              this.dataList.businessLicense.businessLicenseId = business.businessLicenseId;
              // 有效期按钮转换
              let time = business.validPermanent;
              // console.log(time,"有效期")
              if (time) {
                this.validPermanent = "2";
              } else {
                this.validPermanent = "1";
                this.validPeriod = business.validPeriod;
              }
            } else {
              this.dataList.businessLicense = {};
              this.licenseImg = [];
            }
            // 陈康
                // this.dataList.recordValidTerm=data.recordValidTerm.slice(0,11)
  console.log(data.recordValidTerm,"备案证")
            if (data.foodBusinessLicense != "" && data.foodBusinessLicense != null) {
              if (data.foodBusinessLicense.imagesUrl) {
                let food = data.foodBusinessLicense;
                this.foodBusinessLicense = food;
                this.dataList.foodBusinessLicense.validityTerm = food.validityTerm;
                this.licenseKey = [{ url: this.foodBusinessLicense.imagesUrl }];
                this.showLicenseKey = true;
              } else {
                this.edit = false; //是否显示许可证
                this.showLicenseKey = false;
              }
                console.log(data.recordValidTerm,"recordValidT备案证")
              
            }
            if(data.recordValidTerm){
                console.log(data.recordValidTerm,"recordValidTerm")
                this.dataList.recordValidTerm=data.recordValidTerm.slice(0,10)
              }
                console.log(this.dataList.recordValidTerm,"备案证1")
            //银行信息
            if (data.appointmentBank) {
              this.isOpen = "true";
              let bankInfo = data.appointmentBank;
              this.appointmentBank.appointmentTime = bankInfo.appointmentTime;
              this.appointmentBank.bankId = parseInt(bankInfo.bankId);
              this.appointmentBank.id = bankInfo.id;

              this.appointmentBank.merchantId = parseInt(bankInfo.merchantId);
            } else {
              this.isOpen = "false";
              this.appointmentBank.appointmentTime = "";
              this.appointmentBank.bankId = "";
            }
            
            // 行业
            let hangye = data.categoryInfoMap;
            if (hangye !== undefined) {
              // this.hangyeArr = hangye;
              // console.log(hangye[1], "hangye");
              let hangyeIdArr = [];
              let hangyeNameArr = [];
              if (hangye[1] == null) {
              } else {
                this.youhangye = true;
                for (let i in hangye) {
                  hangyeIdArr.push(hangye[i].categoryId);
                  hangyeNameArr.push(hangye[i].categoryName);
                }
                let hangyeTwoId = hangyeIdArr[0];
                // let hangyeTwo=hangyeNameArr[0]
                if (hangyeTwoId == "54") {
                  this.hangye.one = hangyeNameArr[0]; // 行业一级名字
                  this.hangye.two = "无";
                  this.hangye.three = "无";
                  this.hangyetwoBtn = true; //禁用二级行业下拉
                  this.hangyeThreeBtn = true; //禁用二级行业下拉
                } else {
                  this.hangye.one = hangyeNameArr[0]; // 行业一级名字
                  //  console.log(hangyeNameArr[1],"hangyeNameArr[1]")
                  if (!hangyeNameArr[1]) {
                    this.hangye.two = "";
                  } else {
                    this.hangye.two = hangyeNameArr[1]; // 行业二级名字
                  }
                  if (!hangyeNameArr[2]) {
                    this.hangye.three = "";
                  } else {
                    this.hangye.three = hangyeNameArr[2]; // 行业三级名字
                    if (this.hangye.three.includes("许可")) {
                      this.xuke = true;
                      this.beian = false;
                    } else if (this.hangye.three.includes("备案")) {
                      this.beian = true;
                      this.xuke = false;
                    } else {
                      this.beian = false;
                      this.xuke = false;
                    }
                  }
                  this.hangye_one_categoryId = hangyeIdArr[0];
                  this.hangye_two_categoryId = hangyeIdArr[1];
                  this.hangye_three_categoryId = hangyeIdArr[2];
                }
                this.getHangye();
              }
            }
            //实际经营面积
            let storeArea = data.storeArea;
            if (storeArea == 1) {
              this.storeArea_Name = "150平方米以下";
            } else if (storeArea == 2) {
              this.storeArea_Name = "151-500平方米";
            } else if (storeArea == 3) {
              this.storeArea_Name = "501-1000平方米";
            } else if (storeArea == 4) {
              this.storeArea_Name = "1001-3000平方米";
            } else {
              this.storeArea_Name = "3001以上平方米";
            }
            // 省市区
            let diqu = data.addressInfoMap;
            // console.log(data.addressInfoMap,"diqu88888")
            this.diqu = diqu;
            let diqu_nameArr = [];
            let diqu_idArr = [];
            if (diqu == null) {
            } else {
              this.youdiqu = true;
              for (let y in diqu) {
                diqu_nameArr.push(diqu[y].name);
                diqu_idArr.push(diqu[y].code);
              }
              // console.log(diqu_nameArr.length,"diqu_nameArr")
              //  北京市  东城区
              this.region.province = diqu_nameArr[0]; //  省
              this.region_province_code = diqu_idArr[0];

              if (diqu_nameArr.length == 2) {
                this.region.city = diqu_nameArr[0]; //  市
                this.region_city_code = diqu_idArr[0];
                this.region.area = diqu_nameArr[1]; //  区
                this.region_area_code = diqu_idArr[1];
              } else {
                this.region.city = diqu_nameArr[1]; //  市
                this.region_city_code = diqu_idArr[1];
                this.region.area = diqu_nameArr[2]; //  区
                this.region_area_code = diqu_idArr[2];
              }
              this.getDetailsArea();
            }
          } else {
            console.log(res, "res2");

            load.clear();
          }
        })
        .catch((err) => {
          load.clear();
        });
    },
    //是否显示选择预约开通日期
    showDateSelect() {
      if (this.status == "3" || this.status == "4") {
        this.showApptime = true;
      } else {
        this.showApptime = false;
        this.showDate = true;
      }
    },
    //是否显示选择银行
    showBank() {
      if (this.status == "3" || this.status == "4") {
        this.showApptime = true;
      } else {
        this.showApptime = false;
        this.showBankList = true;
      }
    },
    //返回
    onClickLeft() {
      this.$router.back(); //返回上一页
    },
    // 手机号验证
    formatter(val) {
      const reg = /^1[3456789]\d{9}$/;
      if (this.phone == "1") {
        if (!val) {
          return "";
        }
        if (!reg.test(val)) {
          this.error1 = true;
        } else {
          this.error1 = false;
        }
      }
      return val;
    },
    // 判断是否填完必填项
    bitianfun() {
      console.log(
    
        this.validPermanent,
  
        "222"
      );
      if (this.hangye.three == "请选择") {
        console.log("64747666");
        this.$toast.fail("请选择行业");
      } else if (this.licenseImg == "" || this.licenseImg == null) {
        this.$toast.fail("请上传营业执照");
      } else if (
        (this.afterTime && this.shopHeadPhotoUrl == "") ||
        this.dataList.shopHeadPhotoUrl == ""
      ) {
        this.$toast.fail("请上传店招照片");
      } else if (
        this.dataList.businessLicense.storeName == "" ||
        this.dataList.businessLicense.storeName == null
      ) {
        this.$toast.fail("营业执照名称不能为空");
      } else if (
        this.dataList.businessLicense.representName == "" ||
        this.dataList.businessLicense.representName == null
      ) {
        this.$toast.fail("法人代表人姓名不能为空");
      } else if (
        this.dataList.businessLicense.socialCreditCode == "" ||
        this.dataList.businessLicense.socialCreditCode == null
      ) {
        this.$toast.fail("统一社会信用代码不能为空");
      } else if (
        this.dataList.businessLicense.detailedAddress == "" ||
        this.dataList.businessLicense.detailedAddress == null
      ) {
        this.$toast.fail("地址不能为空");
      } else if (this.validPermanent == "" || this.validPermanent == null) {
        this.$toast.fail("请选择有效期");
      } else if (this.validPermanent == "1" && this.validPeriod == "") {
        console.log("12121");
        this.$toast.fail("请选择证件截止时间");
      } else if (
        this.licenseKey.length > 0 &&
        this.dataList.foodBusinessLicense.validityTerm == null
      ) {
        this.$toast.fail("请选择许可证有效时间");
      } else if (
        this.fileImageUrl.length > 0 &&
        this.dataList.recordValidTerm == null
      ) {
        this.$toast.fail("请选择备案证有效时间");
      } else if (
        this.dataList.storeName == "" ||
        this.dataList.storeName == null
      ) {
        console.log("333");
        this.$toast.fail("店招名称不能为空");
      } else if (
        this.dataList.operatorName == "" ||
        this.dataList.operatorName == null
      ) {
        console.log("14855");
        this.$toast.fail("请填写实际经营人姓名");
      } else if (this.dataList.address == "" || this.dataList.address == null) {
        console.log("1987881");
        this.$toast.fail("请填写实际地址");
      } else if (
        this.dataList.addressCode == "" ||
        this.dataList.addressCode == null
      ) {
        console.log("6666");
        this.$toast.fail("省市区不能为空");
      } else if (
        this.dataList.supervisionName == "" ||
        this.dataList.supervisionName == null
      ) {
        console.log("747485");
        this.$toast.fail("监管所不能为空");
      } else if (this.showShequ && this.dataList.communityName == "") {
        console.log("2589685");
        this.$toast.fail("请选择所属社区");
      } else if (this.isOpen == "") {
        console.log("2589685");
        this.$toast.fail("请选择是否开通支付");
      } else if (this.isOpen == "true" && this.dataList.bankName == "") {
        console.log("2589685");
        this.$toast.fail("请选择预约银行");
      } else if (
        this.isOpen == "true" &&
        this.appointmentBank.appointmentTime == ""
      ) {
        console.log("2589685");
        this.$toast.fail("请选择预约日期");
      } else {
        return true;
      }
    },
    //监听是否开通支付
    isOpenpay(e) {
      console.log(this.isOpen, "是否开通支付");

      this.isOpen = e;
      console.log(this.isOpen, "是否");
      if (this.isOpen == "true") {
        this.dataList.appointmentBank = this.appointmentBank;
      } else {
      }
      // console(this.dataForm,"form")
    },
    // 判断是否开通支付
    panduanIsOpen() {
      console.log(this.isOpen, "this.isOpen222");
      if (this.isOpen == "true") {
        // this.dataForm.appointmentBank=this.appointmentBank
        // this.dataForm.bankName=this.dataList.bankName

        if (this.dataList.bankName == "" || this.dataList.bankName == null) {
          this.$toast.fail("请选择预约银行");
        } else if (
          this.appointmentBank.appointmentTime == "" ||
          this.appointmentBank.appointmentTime == null
        ) {
          this.$toast.fail("请选择预约开通日期");
        } else {
          return true;
        }
      } else {
        return true;
      }
    },
    //备注 确认弹框   certificateRemark: "", //备注 单选按钮
    // certificateRemarkOther: "", //备注内容
    onConfirm() {
      this.noteLoading = true;
      if (
        this.dataList.certificateRemark == "" ||
        this.dataList.certificateRemarkOther == ""
      ) {
        this.$toast.fail("请完善表单内容！");
        this.noteLoading = false;
      } else {
        this.showNote = false;
        this.noteLoading = false;
        this.showDialog = true;
      }
    },
    // 提交
    onCommit() {
      if (this.bitianfun() && this.panduanIsOpen()) {
        if (this.xuke && this.licenseKey.length == 0) {
          this.showNote = true;
        } else if (this.beian && this.fileImageUrl.length == 0) {
          this.showNote = true;
        } else {
          this.showNote = false;
          this.dataList.certificateRemarkOther=''
          this.dataList.certificateRemark=''
          this.showDialog = true; //下一步
        }
      }
    },
    //选择定位
    selectPosition() {
      this.enter = false;
      let data = this.dataList.address;
    },

    //二次确认
    beforeClose(action, done) {
      if (action === "cancel") {
        //取消按钮
        done();
      } else if (action === "confirm") {
        let dataList = this.dataList;
        this.show = false;
        //确定按钮
        let upDataForm = this.$toast.loading({
          duration: 0,
          forbidClick: true,
          message: "提交中...",
        });
        if (this.validPermanent == "2") {
          this.validPermanent = true; //永久
          this.validPeriod = "2999-01-01";
        } else {
          this.validPermanent = false;
        }
        dataList.businessLicense.validPeriod = this.validPeriod;
        if (this.isOpen == "true") {
          dataList.appointmentBank = this.appointmentBank;
        }
        dataList.businessLicense.validPermanent = this.validPermanent;
        //  console.log(this.licenseKey,this.licenseKey,"this.licenseKey")
        if (this.licenseKey == "" || this.licenseKey == null) {
          this.foodBusinessLicens = {};
        } else {
          dataList.foodBusinessLicense = this.foodBusinessLicense;
        }
        dataList.operatingState = !this.operatingState; //是否正常营业
        if (!this.showShequ) {
          dataList.communityName = "";
          dataList.communityId = "";
        }
        this.$api
          .mechEdit(dataList)
          .then((res) => {
            if (res.data.status == 200) {
              this.$toast({
                message: "保存成功",
                icon: "passed",
                duration: 1000,
              });
              this.$router.back();
            } else {
              upDataForm.clear();
              this.$toast({
                message: "保存失败,请重新保存",
                icon: "close",
                duration: 200,
              });
            }
          })
          .catch((err) => {
            upDataForm.clear();
            setTimeout(() => {
              this.$toast({
                message: "保存失败",
                icon: "close",
              });
            }, 200);
            if (this.validPermanent == "true") {
              this.validPermanent = "2"; //永久
            } else {
              this.validPermanent = "1";
            }
          });
      }
      done();
    },
    //面积下拉数据
    getAreaOption(value) {
      this.dataList.storeArea = parseInt(value.id);
      this.storeArea_Name = value.area;
      this.showStoreArea = false;
    },
    add0(val) {
      return val < 10 ? "0" : "";
    },
    //预约时间下拉数据
    onDate(date) {
      this.showDate = false;
      let month =
        date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1;
      let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
      this.appointmentBank.appointmentTime = `${date.getFullYear()}-${month}-${day}`;
    },
    //有效期截止时间下拉数据
    getSelectDate(date){
  console.log(this.number,"youxiaoqi")
  this.showvalidDate=false
  let month =
        date.getMonth() + 1 < 10
          ? "0" + (date.getMonth() + 1)
          : date.getMonth() + 1;
      let day = date.getDate() < 10 ? "0" + date.getDate() : date.getDate();
    if (this.number == "3") {
        //营业执照
        this.validPeriod = `${date.getFullYear()}-${month}-${day}`;
      }
      if (this.number == "1") {
        //许可证
        this.dataList.foodBusinessLicense.validityTerm = `${date.getFullYear()}-${month}-${day}`;
      }
      if (this.number == "2") {
        //备案证
        this.dataList.recordValidTerm = `${date.getFullYear()}-${month}-${day}`;
      }
    },
    //年月日格式化
    formatterDate(type, val) {
      if (type === 'year') {
        return val + '年';
      }
      if (type === 'month') {
        return val + '月';
      }
      if (type === 'day') {
        return val + '日';
      }
      return val;
    },
    //获取当前时间
    getToday() {
      let date = new Date();
      let year = date.getFullYear();
      let month = date.getMonth();
      let day = date.getDate();
      this.defaultDate = new Date(year, month, day);
    },
   
    //获取银行数据
    getBankInfo() {
      this.$api.bankList().then((res) => {
        this.bankList = res.data.data;
      });
    },
    //银行下拉数据
    getBank(values) {
      this.showBankList = false;
      this.dataList.bankName = values.bankName;
      this.dataList.bankType = parseInt(values.bankCode);
      this.appointmentBank.bankId = parseInt(values.bankCode);
    },
    // 经营地址API
    postPlace() {
      let data = {};
      data = {
        parentCode: 0,
        level: 1,
      };
      this.$api.areaList(data).then((res) => {
        let data = res.data.data;
        this.listProvince = data; //省
      });
    },

    // 省级确认下拉数据
    onProvince(values) {
      this.dataList.supervisionName = "";
      this.dataList.communityName = "";
      this.showProvince = false;
      this.showShequ = false;
      this.region.province = values.name;
      //请求市的数据
      this.region.city = "请选择市";
      this.region.area = "请选择区";
      let data = {
        parentCode: values.code,
        level: 2,
      };
      this.$api.areaList(data).then((res) => {
        let data = res.data.data;
        this.listCity = data; //市
      });
    },
    //先限制省级选完
    showCityList() {
      if (this.region.province == "请选择省" || this.region.province == null) {
        this.$toast.fail({ message: "请先选择省", duration: 600 });
      } else {
        this.showCity = true;
      }
    },
    // 市级下拉数据
    onCity(values) {
      this.dataList.supervisionName = "";
      this.dataList.communityName = "";
      this.showCity = false;
      this.showShequ = false;
      this.region.city = values.name;
      this.region.area = "请选择区";
      //请求区的数据
      let data = {
        parentCode: values.code,
        level: 3,
      };
      this.$api.areaList(data).then((res) => {
        let data = res.data.data;
        this.listArea = data; //区
      });
      // 解除字符串变回数组
      values.value = JSON.parse(values.children_one);
      this.listArea = values.value;
    },
    //先限制市级选完
    showAreaList() {
      if (this.region.city == "请选择市" || this.region.city == null) {
        this.$toast.fail({ message: "请先选择市", duration: 600 });
      } else {
        this.showArea = true;
      }
    },
    // 区级下拉数据
    onArea(values) {
      this.dataList.supervisionName = "";
      this.dataList.communityName = "";
      this.showArea = false;
      this.showShequ = false;
      // input显示
      this.region.area = values.name;
      // 赋值到表单里面
      this.dataList.addressCode = values.code;
      console.log(this.dataList.addressCode, "气度code");
      this.govGetList(values.code);
    },
    selectSupervision() {
      console.log(222222);
      if (
        this.dataList.addressCode == "" ||
        this.region.province == "请选择省" ||
        this.region.city == "请选择市" ||
        this.region.area == "请选择区"
      ) {
        this.showgov = false;
        this.$toast.fail({ message: "请先选择省市区", duration: 600 });
      } else {
        this.showgov = true;
      }
      console.log(this.showGov, "showGov");
    },
    // 限制选择社区
    selectCommunit() {
      if (
        this.dataList.supervisionId == "" ||
        this.dataList.supervisionName == ""
      ) {
        this.showcommunit = false;
        this.$toast.fail({ message: "请先选择监管所", duration: 600 });
      } else {
        this.showcommunit = true;
        this.getCommunit(this.dataList.supervisionId);
      }
    },
    // 所属监管所API
    govGetList(id) {
      this.dataList.supervisionName = "";
      let govId = {
        addressId: id,
      };
      // 获取监管所API
      this.$api.supervisionList(govId).then((res) => {
        this.listGov = res.data.data;
      });
    },
    // 所属监管所方法
    onGov(values) {
      this.dataList.communityName = "";
      this.dataList.supervisionId = values.supervisionId;
      this.dataList.supervisionName = values.supervisionName;
      this.showgov = false;
      this.getCommunit(this.dataList.supervisionId); //社区
    },
    // 所属社区方法
    onCommunit(values) {
      this.dataList.communityId = values.communityId;
      this.dataList.communityName = values.communityName;
      this.showcommunit = false;
      console.log(values);
    },
    getCommunit(id) {
      this.dataList.communityName = "";
      console.log(id, "社区id");
      let communityId = {
        pid: id,
      };
      // 获取社区API
      this.$api.communitList(communityId).then((res) => {
        console.log(res.data.data, "社区");
        if (res.data.data.length <= 0) {
          this.showShequ = false;
        } else {
          this.showShequ = true;
          this.listCommunit = res.data.data;
        }
      });
    },
    // 组件方法 获取 流
    afterRead(file, name) {
      this.files.name = file.file.name; // 获取文件名
      this.files.type = file.file.type; // 获取类型
      this.imgPreview(file.file, name.name);
    },
    // 处理图片
    imgPreview(file, name) {
      let self = this;
      let Orientation;
      //去获取拍照时的信息，解决拍出来的照片旋转问题   npm install exif-js --save   这里需要安装一下包
      Exif.getData(file, function () {
        Orientation = Exif.getTag(this, "Orientation");
      });
      // 看支持不支持FileReader
      if (!file || !window.FileReader) return;
      if (/^image/.test(file.type)) {
        // 创建一个reader
        let reader = new FileReader();
        // 将图片2将转成 base64 格式
        reader.readAsDataURL(file);
        // 读取成功后的回调
        reader.onloadend = function () {
          let result = this.result;
          let img = new Image();
          img.src = result;
          //判断图片是否大于500K,是就直接上传，反之压缩图片
          if (this.result.length <= 500 * 1024) {
            // 上传图片
            self.postImg(this.result, name);
          } else {
            img.onload = function () {
              let data = self.compress(img, Orientation);
              // 上传图片
              self.postImg(data, name);
            };
          }
        };
      }
    },
    // 压缩图片
    compress(img, Orientation) {
      let canvas = document.createElement("canvas");
      let ctx = canvas.getContext("2d");
      //瓦片canvas
      let tCanvas = document.createElement("canvas");
      let tctx = tCanvas.getContext("2d");
      // let initSize = img.src.length;
      let width = img.width;
      let height = img.height;
      //如果图片大于四百万像素，计算压缩比并将大小压至400万以下
      let ratio;
      if ((ratio = (width * height) / 4000000) > 1) {
        // console.log("大于400万像素");
        ratio = Math.sqrt(ratio);
        width /= ratio;
        height /= ratio;
      } else {
        ratio = 1;
      }
      canvas.width = width;
      canvas.height = height;
      //    铺底色
      ctx.fillStyle = "#fff";
      ctx.fillRect(0, 0, canvas.width, canvas.height);
      //如果图片像素大于100万则使用瓦片绘制
      let count;
      if ((count = (width * height) / 1000000) > 1) {
        // console.log("超过100W像素");
        count = ~~(Math.sqrt(count) + 1); //计算要分成多少块瓦片
        //      计算每块瓦片的宽和高
        let nw = ~~(width / count);
        let nh = ~~(height / count);
        tCanvas.width = nw;
        tCanvas.height = nh;
        for (let i = 0; i < count; i++) {
          for (let j = 0; j < count; j++) {
            tctx.drawImage(
              img,
              i * nw * ratio,
              j * nh * ratio,
              nw * ratio,
              nh * ratio,
              0,
              0,
              nw,
              nh
            );
            ctx.drawImage(tCanvas, i * nw, j * nh, nw, nh);
          }
        }
      } else {
        ctx.drawImage(img, 0, 0, width, height);
      }
      //修复ios上传图片的时候 被旋转的问题
      if (Orientation != "" && Orientation != 1) {
        switch (Orientation) {
          case 6: //需要顺时针（向左）90度旋转
            this.rotateImg(img, "left", canvas);
            break;
          case 8: //需要逆时针（向右）90度旋转
            this.rotateImg(img, "right", canvas);
            break;
          case 3: //需要180度旋转
            this.rotateImg(img, "right", canvas); //转两次
            this.rotateImg(img, "right", canvas);
            break;
        }
      }
      //进行最小压缩
      let ndata = canvas.toDataURL("image/jpeg", 0.8);
      tCanvas.width = tCanvas.height = canvas.width = canvas.height = 0;
      // console.log(ndata);
      return ndata;
    },
    // 旋转图片
    rotateImg(img, direction, canvas) {
      //最小与最大旋转方向，图片旋转4次后回到原方向
      const min_step = 0;
      const max_step = 3;
      if (img == null) return;
      //img的高度和宽度不能在img元素隐藏后获取，否则会出错
      let height = img.height;
      let width = img.width;
      let step = 2;
      if (step == null) {
        step = min_step;
      }
      if (direction == "right") {
        step++;
        //旋转到原位置，即超过最大值
        step > max_step && (step = min_step);
      } else {
        step--;
        step < min_step && (step = max_step);
      }
      //旋转角度以弧度值为参数
      let degree = (step * 90 * Math.PI) / 180;
      let ctx = canvas.getContext("2d");
      switch (step) {
        case 0:
          canvas.width = width;
          canvas.height = height;
          ctx.drawImage(img, 0, 0);
          break;
        case 1:
          canvas.width = height;
          canvas.height = width;
          ctx.rotate(degree);
          ctx.drawImage(img, 0, -height);
          break;
        case 2:
          canvas.width = width;
          canvas.height = height;
          ctx.rotate(degree);
          ctx.drawImage(img, -width, -height);
          break;
        case 3:
          canvas.width = height;
          canvas.height = width;
          ctx.rotate(degree);
          ctx.drawImage(img, -width, 0);
          break;
      }
    },
    //将base64转换为文件
    dataURLtoFile(dataurl, name) {
      var arr = dataurl.split(","),
        bstr = atob(arr[1]),
        n = bstr.length,
        u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new File([u8arr], this.files.name, {
        type: this.files.type,
      });
    },
    // 提交图片到后端
    postImg(base64, name) {
      let file = this.dataURLtoFile(base64, name);
      let formData = new window.FormData();
      formData.append("file", file);
      const toast = this.$toast.loading({
        duration: 0,
        forbidClick: true,
        message: "上传图片中...",
      });

      if (name == "1") {
        // 上传图片
        this.$api
          .uploadImg(formData)
          .then((res) => {
            if (res.data.status == 200) {
              this.$toast.success({
                message: "上传成功",
                duration: 2000,
              });
              let dataImg = res.data.data;
              this.dataList.businessLicense = dataImg;
              // this.dataList.address=dataImg.detailedAddress   //实际地址
              this.dataList.businessLicense.imagesUrl = dataImg.imagesUrl;
              // 清除加载
              toast.clear();
            } else {
              this.licenseImg = [];
              this.$toast.fail(res.data.message);
            }
          })
          .catch((err) => {
            // 清除加载
            toast.clear();
            this.licenseImg = [];
             this.$toast("网络情况不好，请重新拍照上传");
            
          });
      }
      if (name == "2") {
        // 上传 店铺照片图片
        this.$api
          .uploadShopImg(formData)
          .then((res) => {
            console.log(res.data.data, "上传图片");
            if (res.data.status == 200) {
              this.$toast.success({
                message: "上传成功",
                duration: 2000,
              });
              this.dataList.shopHeadPhotoUrl = res.data.data; //店招照片
              // 清除加载
              toast.clear();
            } else {
              this.dataList.shopHeadPhotoUrl = "";
              this.shopHeadPhotoUrl = [];
              this.$toast.fail(res.data.message);
            }
          })
          .catch((err) => {
            this.dataList.shopHeadPhotoUrl = "";
            this.shopHeadPhotoUrl = [];
            // 清除加载
            toast.clear();
            this.$toast("网络情况不好，请重新拍照上传");
            console.log("失败");
          });
      }
      if (name == "3") {
        // 上传 许可证图片
        this.$api
          .uploadFoodImg(formData)
          .then((res) => {
            console.log(res.data.data, "上传图片");
            if (res.data.status == 200) {
              this.$toast.success({
                message: "上传成功",
                duration: 2000,
              });
              let dataFoodImg = res.data.data;
              this.foodBusinessLicense = dataFoodImg;
              this.dataList.foodBusinessLicense= dataFoodImg
              this.dataList.foodBusinessLicense.validityTerm = dataFoodImg.validToDate.slice(0, 10);
              console.log(this.dataList.foodBusinessLicense, "许可证");
              console.log(dataFoodImg.validToDate.slice(0,10),"ck")
              // 清除加载
              toast.clear();
            } else {
              this.$toast.fail(res.data.message);
            }
          })
          .catch((err) => {
            // 清除加载
            toast.clear();
          });
      }
      if (name == "5") {
        this.$api
          .uploadOtherImg(formData)
          .then((res) => {
            console.log(res.data.data, "zhap");
            if (res.data.status == 200) {
              this.$toast.success({
                message: "上传成功",
                duration: 2000,
              });
              this.dataList.hygieneImageUrl = res.data.data; //卫生证
              // 清除加载
              toast.clear();
            } else {
              this.$toast.fail(res.data.message);
              this.hygieneImageUrl = [];
            }
          })
          .catch((err) => {
            // 清除加载
            toast.clear();
            this.$toast("网络情况不好，请重新拍照上传");
             this.hygieneImageUrl = [];
            console.log("失败");
          });
      }
      if (name == "4") {
        this.$api
          .uploadOtherImg(formData)
          .then((res) => {
            console.log(res.data.data, "zhap");
            if (res.data.status == 200) {
              this.$toast.success({
                message: "上传成功",
                duration: 2000,
              });
              this.dataList.fileImageUrl = res.data.data; //备案证
              // 清除加载
              toast.clear();
            } else {
              this.$toast.fail(res.data.message);
              this.fileImageUrl = []; //备案证
            }
          })
          .catch((err) => {
            // 清除加载
            toast.clear();
            this.$toast("网络情况不好，请重新拍照上传");
            console.log("失败");
            this.fileImageUrl = []; //备案证
          });
      }
    },

    // 获取行业
    onIndustryCategory() {
      console.log(this.industryType, "1");
      let data = {
        categoryId: 0,
      };
      this.$api.industryList(data).then((res) => {
        this.oneLevelhangye = res.data.data;
      });
    },
    //一级行业下拉
    oneLevel(value) {
      console.log(value, "value");
      let categoryId = value.categoryId;
      this.showOne = false;
      this.hangye.one = value.categoryName;
      this.beian = false;
      this.xuke = false;
      this.hangye.two = "请选择";
      this.hangye.three = "请选择";
      console.log(this.hangye.one, "this.hangye.one");
      let data = {
        categoryId: value.categoryId,
      };
      this.$api.industryList(data).then((res) => {
        let rr = res.data.data;
        if (rr.length <= 0) {
          this.hangyetwoBtn = true;
          this.hangyeThreeBtn = true;
          this.hangye.two = "无";
          this.hangye.three = "无";
          this.showTwo = false;
          this.showThree = false;
        } else {
          this.twoLevelhangye = res.data.data;
        }
        this.dataList.industryType = parseInt(value.categoryId);
        this.dataList.industryCate = parseInt(value.categoryId);
        console.log(this.twoLevelhangye, "行业2");
      });
    },
    //限制先选上一行业
    showTwoList() {
      if (this.hangye.one == "请选择" || this.hangye.one == null) {
        this.$toast.fail({ message: "请先选择上一级行业", duration: 600 });
      } else if (this.hangye.two == "无") {
        this.hangyetwoBtn = true;
      } else {
        this.showTwo = true;
        this.hangyetwoBtn = false;
      }
    },
    //限制先选上一行业
    showThreeList() {
      if (this.hangye.two == "请选择" || this.hangye.two == null) {
        this.$toast.fail({ message: "请先选择上一级行业", duration: 600 });
      } else if (this.hangye.three == "无") {
        this.hangyeThreeBtn = true;
      } else {
        this.showThree = true;
        this.hangyeThreeBtn = false;
      }
    },
    //二级行业下拉 确定
    twoLevel(value) {
      console.log(value, "value2");
      this.showTwo = false;
      this.hangye.two = value.categoryName;
      this.beian = false;
      this.xuke = false;
      this.hangyeTwoId = value.categoryId;
      if ( this.hangyeTwoId == "54") {
        this.hangyeThreeBtn = true;
        this.hangye.three = "无";
      } else {
        this.hangye.three = "请选择";
        let data = {
          categoryId: value.categoryId,
        };
        this.$api.industryList(data).then((res) => {
          let rr = res.data.data;
          if (rr.length <= 0) {
            this.hangyeThreeBtn = true;
            this.hangye.three = "无";
            // this.threeLevelhangye={}
            this.showThree = false;
            //  this.dataForm.industryType = parseInt(value.categoryId);
          } else {
            this.threeLevelhangye = res.data.data;
          }

          console.log(this.threeLevelhangye, "行业3");
        });
      }
      this.dataList.industryType = parseInt(value.categoryId);
    },

    //三级行业下拉数据集
    threeLevel(value) {
      console.log(value, "value3");
      this.showThree = false;
      if (this.hangye.two == "无") {
        this.hangye.three = "无";
        this.hangyeThreeBtn = true;
      } else {
        this.hangye.three = "请选择";
      }
      this.hangye.three = value.categoryName;
      if (this.hangye.three.includes("许可")) {
        this.xuke = true;
        this.beian = false;
      } else if (this.hangye.three.includes("备案")) {
        this.beian = true;
        this.xuke = false;
      } else {
        this.beian = false;
        this.xuke = false;
      }
      this.dataList.industryType = parseInt(value.categoryId);
      // this.industryType_name = value.categoryName;
    },
    //判断是否点击编辑
    isEdit() {
      this.edit = true;
      this.showRight = false;
      this.showJy = true;
      this.showSuper = true;
      this.showOther = true;
      if (this.hangye.two == "") {
        this.hangye.two = "请选择";
      }
      if (this.hangye.three == "") {
        this.hangye.three = "请选择";
      }
    },
    delImg(file, name) {
      console.log(file, name, "shanchu");
      // 删除证件
      let n = name.name;
      if (n == "1") {
        this.licenseImg = [];
        this.dataList.businessLicense = {};
      } else if (n == "2") {
        this.shopHeadPhotoUrl = [];
      } else if (n == "3") {
        this.licenseKey = [];
        this.foodBusinessLicense = {};
        this.dataList.foodBusinessLicense = {};
      } else if (n == "4") {
        this.$dialog
          .confirm({
            message: "确认删除吗？",
          })
          .then(() => {
            let id = {
              id: this.dataList.merchantId,
            };
            this.$api.delBeian(id).then((res) => {
              this.fileImageUrl = [];
              this.dataList.fileImageUrl = "";
              this.dataList.recordValidTerm=''
            });
          })
          .catch(() => {});
      } else if (n == "5") {
        this.$dialog
          .confirm({
            message: "确认删除吗？",
          })
          .then(() => {
            let id = {
              id: this.dataList.merchantId,
            };
            this.$api.delHygiene(id).then((res) => {
              this.hygieneImageUrl = [];
              this.dataList.hygieneImageUrl = "";
            });
          })
          .catch(() => {
            // on cancel
          });
      }
    },
  },
};
</script>

<style scoped>
.enter {
  color: rgba(38, 38, 38, 1);
  left: 0;
  right: 0;
  background: #fff;
}
.two {
  background: #fff;
  position: fixed;
  top: 20%;
  bottom: 0;
  left: 0;
  right: 0;
}
/* 头部文字 */
/deep/ .van-nav-bar__title {
  font-size: 18px;
  font-weight: bold;
  color: rgba(38, 38, 38, 1);
}
/* 返回键 */
/deep/ .van-icon-arrow-left::before {
  font-size: 18px;
  content: "\F008";
  color: rgba(38, 38, 38, 1);
  /* margin-bottom: 5px; */
}
.setBackground {
  background-color: #347ff1 !important;
}
/* 证件上传 */
.upload {
  display: flex;
  flex-wrap: wrap;
}
.uploadImg {
  display: inline-block;
  flex-wrap: wrap;
  margin: 10px 0px 0px 0px;
}
.border {
  border-top: 10px solid #f5f6f7;
}
/* 下一步按钮 */
.loginBtn {
  background: rgba(52, 127, 241, 0.3);
  color: #fff;
  margin-top: 20px;
  font-size: 16px;
}
.divborder {
  border-top: 8px solid #f5f6f7;
}
.topflex {
  font-size: 16px;
  color: rgba(38, 38, 38, 1);
  font-weight: bolder;
  padding: 10px 10px 10px 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.bottomflex {
  justify-content: space-around;
  color: rgba(140, 140, 140, 1);
  background: rgba(245, 246, 247, 1);
  padding: 20px;
}
/* 展开  收起 */
.speard {
  color: rgba(52, 127, 241, 1);
  vertical-align: middle;
  display: inline-block;
  font-weight: lighter;
  font-size: 14px;
}
.icon {
  position: relative;
  top: 3px;
  display: inline-block;
  margin-left: 2px;
}
/* 证件信息栏  */
.van-cell:not(:last-child)::after {
  border-bottom: none;
}
/* 定位图标 */
/deep/ .van-icon-location-o::before {
  content: "\F07C";
  color: #1989fa !important;
}
/* 弹框 单选按钮 */
/deep/ .van-radio {
  margin-bottom: 10px;
}
/deep/ .van-button::before {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 100%;
  height: 100%;
  background-color: #1989fa !important;
  border: inherit;
  border-color: #000;
  border-radius: inherit;
  -webkit-transform: translate(-50%, -50%);
  transform: translate(-50%, -50%);
  opacity: 0;
  content: " ";
}
.flexBtn {
  display: flex;
  margin-top: 30px;
}
.footerBtn {
  background: #fff;
  color: #262626;
  border: 1px solid rgba(52, 127, 241, 1);
  width: 100%;
  margin-right: 10px;
}
.footerOk {
  background: rgba(52, 127, 241, 1);
  border: 1px solid rgba(52, 127, 241, 1);
  color: #fff;
  width: 100%;
}
</style>
