<%-- class_list.jsp の完全修正版 - インラインスタイル --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
  <c:param name="title">クラス一覧</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">クラス管理</h2>

      <!-- 新規登録リンク -->
      <div class="my-2 text-end px-4">
        <a href="ClassCreate.action" class="btn btn-primary btn-sm">新規登録</a>
      </div>

      <!-- クラス一覧表示 -->
      <c:choose>
        <c:when test="${not empty classList && classList.size() > 0}">
          <table style="width: 100%; border-collapse: collapse; margin-bottom: 20px; border: 1px solid #e0e0e0;">
            <tr>
              <th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;">クラス番号</th>
              <th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;">学生数</th>
              <th style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; font-weight: normal; background-color: #ffffff;"></th>
            </tr>
            <c:forEach var="classItem" items="${classList}">
              <tr>
                <td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">${classItem.class_num}</td>
                <td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">${classItem.c_count}</td>
                <td style="border: 1px solid #e0e0e0; padding: 10px 15px; text-align: left; background-color: #ffffff;">
                  <a href="ClassUpdate.action?class_num=${classItem.class_num}" style="color: #0000ff; text-decoration: none;">変更</a>
                </td>
              </tr>
            </c:forEach>
          </table>
        </c:when>
        <c:otherwise>
          <div class="alert alert-info">
            クラス情報がありません。
          </div>
        </c:otherwise>
      </c:choose>

      <div class="mt-3">
        <a href="Menu.action" class="btn btn-secondary">メニューに戻る</a>
      </div>
    </section>
  </c:param>
</c:import>