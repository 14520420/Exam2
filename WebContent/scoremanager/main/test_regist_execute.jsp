<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
  <c:param name="title">得点管理システム</c:param>
  <c:param name="scripts"></c:param>

  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">成績管理</h2>

      <!-- 検索条件表示 -->
      <div class="row border mx-3 py-2 mb-3 rounded align-items-center">
        <div class="col-md-3">
          <label class="form-label">入学年度</label>
          <select class="form-select" disabled>
            <option>${selectedEntYear}</option>
          </select>
        </div>
        <div class="col-md-2">
          <label class="form-label">クラス</label>
          <select class="form-select" disabled>
            <option>${selectedClassNum}</option>
          </select>
        </div>
        <div class="col-md-3">
          <label class="form-label">科目</label>
          <select class="form-select" disabled>
            <option>${subject.name}</option>
          </select>
        </div>
        <div class="col-md-2">
          <label class="form-label">回数</label>
          <select class="form-select" disabled>
            <option>${selectedNo}</option>
          </select>
        </div>
        <div class="col-md-2 text-center">
          <a href="TestRegist.action" class="btn btn-secondary mt-4">戻る</a>
        </div>
      </div>

      <!-- 科目情報表示 -->
      <div class="mx-3 mb-3">
        <p><strong>科目：${subject.name}（${selectedNo}回）</strong></p>
      </div>

      <!-- 成績入力フォーム -->
      <form action="TestRegistDone.action" method="post">
        <!-- hidden fields -->
        <input type="hidden" name="ent_year" value="${selectedEntYear}">
        <input type="hidden" name="class_num" value="${selectedClassNum}">
        <input type="hidden" name="subject_cd" value="${selectedSubjectCd}">
        <input type="hidden" name="no" value="${selectedNo}">

        <!-- 学生リスト・点数入力 -->
        <div class="mx-3 mb-3">
          <table class="table table-bordered table-striped">
            <thead class="table-light">
              <tr>
                <th>入学年度</th>
                <th>クラス</th>
                <th>学生番号</th>
                <th>氏名</th>
                <th>点数</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="item" items="${studentScoreList}" varStatus="status">
                <tr>
                  <td>${selectedEntYear}</td>
                  <td>${selectedClassNum}</td>
                  <td>${item.student.no}
                    <input type="hidden" name="student_no" value="${item.student.no}">
                  </td>
                  <td>${item.student.name}</td>
                  <td>
                    <input type="text" class="form-control" name="point"
                           value="${item.point}" style="width: 200px;">
                    <c:if test="${not empty item.error}">
                      <div style="color: red; font-size: 12px; margin-top: 5px;">
                        ${item.error}
                      </div>
                    </c:if>
                    <c:if test="${status.index == 0 && empty hasErrors}">
                      <div style="color: red; font-size: 12px; margin-top: 5px;">
                        0～100の範囲で入力してください
                      </div>
                    </c:if>
                  </td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>

        <div class="mx-3 mb-3">
          <button type="submit" class="btn btn-secondary">登録して終了</button>
        </div>
      </form>
    </section>
  </c:param>
</c:import>