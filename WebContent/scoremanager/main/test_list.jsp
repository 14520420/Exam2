<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:import url="/common/base.jsp">
  <c:param name="title">得点一覧</c:param>
  <c:param name="content">
    <section class="me-4">

      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">得点一覧（科目）</h2>

      <!-- 検索フォーム -->
      <form method="get">
        <div class="row border mx-3 py-2 rounded mb-2 align-items-center">
          <!-- 入学年度 -->
          <div class="col-3">
            <label class="form-label">入学年度</label>
            <select name="f1" class="form-select">
              <option value="">-------</option>
              <c:forEach var="y" items="${ent_year_set}">
                <option value="${y}" <c:if test="${conditions['entYear']==y.toString()}">selected</c:if>>${y}</option>
              </c:forEach>
            </select>
          </div>

          <!-- クラス -->
          <div class="col-3">
            <label class="form-label">クラス</label>
            <select name="f2" class="form-select">
              <option value="">-------</option>
              <c:forEach var="c" items="${class_num_set}">
                <option value="${c}" <c:if test="${conditions['classNum']==c}">selected</c:if>>${c}</option>
              </c:forEach>
            </select>
          </div>

          <!-- 科目 -->
          <div class="col-3">
            <label class="form-label">科目</label>
            <select name="f3" class="form-select">
              <option value="">-------</option>
              <c:forEach var="s" items="${subjects}">
                <option value="${s.cd}" <c:if test="${conditions['subjectCd']==s.cd}">selected</c:if>>${s.name}</option>
              </c:forEach>
            </select>
          </div>

          <!-- 回数 -->
          <div class="col-3">
            <label class="form-label">回数</label>
            <select name="f4" class="form-select">
              <option value="">-------</option>
              <c:forEach var="n" items="${no_set}">
                <option value="${n}" <c:if test="${conditions['no']==n.toString()}">selected</c:if>>${n}</option>
              </c:forEach>
            </select>
          </div>
        </div>

        <div class="row mx-3 text-center mb-3">
          <div class="col-12">
            <button class="btn btn-secondary" id="filter-button">検索</button>
          </div>
        </div>
      </form>

      <!-- エラーメッセージ表示 -->
      <c:if test="${not empty error}">
        <div class="alert alert-danger mx-3">
          ${error}
        </div>
      </c:if>

      <!-- 検索結果 -->
      <c:choose>
        <c:when test="${not empty tests && tests.size() > 0}">
          <div class="mt-2 mx-3">検索結果: ${tests.size()} 件</div>
          <div class="table-responsive mx-3">
            <table class="table table-hover">
              <thead>
                <tr>
                  <th>入学年度</th>
                  <th>クラス</th>
                  <th>学生番号</th>
                  <th>氏名</th>
                  <th>科目</th>
                  <th>回数</th>
                  <th>点数</th>
                </tr>
              </thead>
              <tbody>
                <c:forEach var="t" items="${tests}">
                  <tr>
                    <td>${t.student.entYear}</td>
                    <td>${t.classNum}</td>
                    <td>${t.student.no}</td>
                    <td>${empty t.student.name ? '－' : t.student.name}</td>
                    <td>${t.subject.name}</td>
                    <td>${t.no}</td>
                    <td>${t.point}</td>
                  </tr>
                </c:forEach>
              </tbody>
            </table>
          </div>
        </c:when>
        <c:otherwise>
          <div class="alert alert-info mx-3">
            該当するデータがありません。
          </div>
        </c:otherwise>
      </c:choose>

      <div class="mt-3 mx-3">
        <a href="Menu.action" class="btn btn-secondary">メニューに戻る</a>
      </div>
    </section>
  </c:param>
</c:import>