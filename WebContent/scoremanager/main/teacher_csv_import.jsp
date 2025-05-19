<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:import url="/common/base.jsp">
  <c:param name="title">教員CSVインポート</c:param>
  <c:param name="scripts"></c:param>
  <c:param name="content">
    <section class="me-4">
      <h2 class="h3 mb-3 fw-normal bg-secondary bg-opacity-10 py-2 px-4">教員CSVインポート</h2>

      <div class="mb-4">
        <div class="alert alert-info">
          <h5>CSVファイル形式</h5>
          <p>以下の形式でCSVファイルを作成してください。文字コードはUTF-8を使用してください。</p>
          <p><code>教員ID,パスワード,氏名,学校コード,管理者フラグ</code></p>
          <p>【例】<br>
          <code>teacher1,password1,山田太郎,S001,1</code><br>
          <code>teacher2,password2,佐藤花子,S002,0</code></p>
          <p>※管理者フラグは「1」で管理者、「0」で一般教員となります。</p>
        </div>

        <div class="mt-3">
          <h5>利用可能な学校コード</h5>
          <table class="table table-sm">
            <thead>
              <tr>
                <th>学校コード</th>
                <th>学校名</th>
              </tr>
            </thead>
            <tbody>
              <c:forEach var="school" items="${schoolList}">
                <tr>
                  <td>${school.cd}</td>
                  <td>${school.name}</td>
                </tr>
              </c:forEach>
            </tbody>
          </table>
        </div>
      </div>

      <form action="TeacherCsvImportExecute.action" method="post" enctype="multipart/form-data" class="mb-3">
        <div class="mb-3">
          <label for="csvFile" class="form-label">CSVファイルを選択</label>
          <input type="file" class="form-control" id="csvFile" name="csvFile" accept=".csv" required>
        </div>

        <div class="form-check mb-3">
          <input class="form-check-input" type="checkbox" id="updateExisting" name="updateExisting" value="1">
          <label class="form-check-label" for="updateExisting">
            既存の教員情報を更新する (チェックしない場合は新規登録のみ行います)
          </label>
        </div>

        <button type="submit" class="btn btn-primary">インポート実行</button>
        <a href="TeacherList.action" class="btn btn-secondary ms-2">キャンセル</a>
      </form>

      <c:if test="${not empty error}">
        <div class="alert alert-danger">
          <p>${error}</p>
        </div>
      </c:if>
    </section>
  </c:param>
</c:import>