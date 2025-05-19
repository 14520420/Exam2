<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
  <c:param name="title">教員CSVインポート完了</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-success bg-opacity-25 py-2 px-4">教員CSVインポート完了</h2>

      <div class="mb-4">
        <div class="alert alert-success">
          <h5>処理結果</h5>
          <ul>
            <li>新規登録: ${newCount}件</li>
            <li>更新: ${updateCount}件</li>
            <li>エラー: ${errorCount}件</li>
          </ul>
        </div>

        <c:if test="${not empty errors && errors.size() > 0}">
          <div class="alert alert-warning">
            <h5>エラー内容</h5>
            <ul>
              <c:forEach var="error" items="${errors}">
                <li>${error}</li>
              </c:forEach>
            </ul>
          </div>
        </c:if>
      </div>

      <div class="mt-3">
        <a href="TeacherList.action" class="btn btn-primary">教員一覧に戻る</a>
        <a href="TeacherCsvImport.action" class="btn btn-secondary ms-2">続けてインポート</a>
      </div>
    </section>
  </c:param>
</c:import>